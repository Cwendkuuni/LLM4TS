import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String dirtyHTML = "<html><body><p>Dirty HTML</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body><p>Dirty HTML</p></body></html>", cleanDocument.html());
        
        // Edge case - Empty document
        dirtyHTML = "";
        dirtyDocument = Jsoup.parse(dirtyHTML);
        cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String dirtyHTML = "<html><body><p>Dirty HTML</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
        
        // Edge case - Invalid tag
        dirtyHTML = "<html><body><script>alert('Hello World!');</script></body></html>";
        dirtyDocument = Jsoup.parse(dirtyHTML);
        isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String dirtyHTML = "<html><body><p>Dirty HTML</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element source = dirtyDocument.body();
        Element dest = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(0, numDiscarded);
        assertEquals("<p>Dirty HTML</p>", dest.html());
        
        // Edge case - Element with unsafe child
        whitelist.addTags("p");
        whitelist.addAttributes("p", "style");
        cleaner = new Cleaner(whitelist);
        
        dirtyHTML = "<html><body><div><p style=\"color:red;\">Dirty HTML</p></div></body></html>";
        dirtyDocument = Jsoup.parse(dirtyHTML);
        cleanDocument = Document.createShell(dirtyDocument.baseUri());
        source = dirtyDocument.body();
        dest = cleanDocument.body();
        
        numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(1, numDiscarded);
        assertEquals("<p style=\"color:red;\">Dirty HTML</p>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String dirtyHTML = "<div class=\"container\" style=\"background-color:red;\">Dirty div</div>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        Element sourceEl = dirtyDocument.body().child(0);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;
        
        assertEquals("div", dest.tagName());
        assertEquals(1, numDiscarded);
        assertTrue(dest.hasAttr("class"));
        assertFalse(dest.hasAttr("style"));
        
        // Edge case - No attributes
        dirtyHTML = "<div>Dirty div</div>";
        dirtyDocument = Jsoup.parse(dirtyHTML);
        sourceEl = dirtyDocument.body().child(0);
        
        elementMeta = cleaner.createSafeElement(sourceEl);
        dest = elementMeta.el;
        numDiscarded = elementMeta.numAttribsDiscarded;
        
        assertEquals("div", dest.tagName());
        assertEquals(0, numDiscarded);
        assertFalse(dest.hasAttr("class"));
        assertFalse(dest.hasAttr("style"));
    }
}