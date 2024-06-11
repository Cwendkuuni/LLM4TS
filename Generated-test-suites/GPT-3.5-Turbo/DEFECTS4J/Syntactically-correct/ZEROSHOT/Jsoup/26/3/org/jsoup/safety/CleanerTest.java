import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import java.util.List;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Document.createShell("http://www.example.com");
        dirtyDocument.body().append("<div><p><script>alert('XSS Attack');</script></p></div>");
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertFalse(cleanDocument.toString().contains("<script>"));
        assertTrue(cleanDocument.toString().contains("<div><p></p></div>"));
    }
    
    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Document.createShell("http://www.example.com");
        dirtyDocument.body().append("<div><p><script>alert('XSS Attack');</script></p></div>");
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element source = new Element(Tag.valueOf("div"), "http://www.example.com");
        source.append("<p>This is a paragraph</p>");
        
        Element dest = new Element(Tag.valueOf("div"), "http://www.example.com");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals("<p>This is a paragraph</p>", dest.html());
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element sourceEl = new Element(Tag.valueOf("p"), "http://www.example.com");
        sourceEl.attr("class", "content");
        sourceEl.attr("style", "color: red;");
        
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertEquals("p", meta.el.tagName());
        assertEquals("http://www.example.com", meta.el.baseUri());
        assertEquals(1, meta.numAttribsDiscarded);
    }
}