import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("style"), ""));
        
        Document clean = cleaner.clean(dirtyDocument);
        
        assertEquals("div", clean.body().child(0).tagName());
        assertNull(clean.body().child(1));
        assertNull(clean.body().child(2));
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("script"), ""));
        
        assertTrue(cleaner.isValid(dirtyDocument));
        
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("iframe"), ""));
        
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.appendChild(new Element(Tag.valueOf("p"), ""));
        sourceEl.appendChild(new TextNode("Hello", ""));
        
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
        
        assertEquals(0, numDiscarded);
        assertEquals("p", dest.child(0).tagName());
        assertEquals("Hello", dest.child(1).text());
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://www.example.com");
        sourceEl.attr("onclick", "alert('Hello')");
        
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertEquals(1, meta.el.attributes().size());
        assertTrue(meta.el.attributes().hasKey("href"));
        assertFalse(meta.el.attributes().hasKey("onclick"));
        assertEquals(1, meta.numAttribsDiscarded);
    }
}