
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Element source = new Element("div");
        Element dest = new Element("div");
        source.append("<p>Hello</p><script>alert('XSS')</script>");
        int discarded = cleaner.copySafeNodes(source, dest);
        assertEquals("<div><p>Hello</p></div>", dest.outerHtml());
        assertEquals(1, discarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("div");
        sourceEl.attr("id", "myId");
        sourceEl.attr("onclick", "alert('XSS');");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("div", meta.el.tagName());
        assertEquals(1, meta.numAttribsDiscarded);
        assertEquals("<div id=\"myId\"></div>", meta.el.outerHtml());
    }
}
