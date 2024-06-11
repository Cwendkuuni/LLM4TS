import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<div><p>Hello</p><script>alert('XSS')</script></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if script tag is removed
        assertEquals(1, cleanDocument.getElementsByTag("script").size());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<div><p>Hello</p><script>alert('XSS')</script></div>");
        
        // Check if the document is considered valid
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create source and destination elements
        Element source = new Element("div");
        Element dest = new Element("div");
        
        // Add a script tag to the source element
        source.append("<script>alert('XSS')</script>");

        // Copy safe nodes and check if script tag is discarded
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div");
        sourceEl.attr("id", "test");

        // Create safe element and check if attributes are filtered correctly
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("div", meta.el.tagName());
        assertEquals(1, meta.numAttribsDiscarded);
    }
}