
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String dirtyHtml = "<html><body><script>alert('Hello!')</script><p>Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body>&lt;script&gt;alert('Hello!')&lt;/script&gt;<p>Test</p></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String safeHtml = "<html><body><p>Test</p></body></html>";
        Document safeDocument = Jsoup.parse(safeHtml);
        
        String unsafeHtml = "<html><body><script>alert('Hello!')</script><p>Test</p></body></html>";
        Document unsafeDocument = Jsoup.parse(unsafeHtml);
        
        assertTrue(cleaner.isValid(safeDocument));
        assertFalse(cleaner.isValid(unsafeDocument));
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testClean_NullDocument_ReturnsNull() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document cleanDocument = cleaner.clean(null);
        
        assertNull(cleanDocument);
    }
    
    @Test
    public void testClean_EmptyDocument_ReturnsEmptyDocument() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testCopySafeNodes_EmptySource_ReturnsZeroDiscarded() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element source = new Element("div");
        Element dest = new Element("div");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement_UnsafeAttribute_DiscardsAttribute() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element("div");
        sourceEl.attr("onclick", "alert('Hello!')");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertEquals("<div></div>", elementMeta.el.toString());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}