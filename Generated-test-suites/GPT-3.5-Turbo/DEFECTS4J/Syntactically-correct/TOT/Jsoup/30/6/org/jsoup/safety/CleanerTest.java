import org.jsoup.Jsoup;
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
    public void testClean_typicalUseCase() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.hasSameValue(dirtyDocument));
        assertNotNull(cleanDocument.body());
        assertEquals("<h1>Welcome!</h1>", cleanDocument.body().html());
    }
    
    @Test
    public void testClean_emptyDocument() {
        String dirtyHtml = "";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.hasSameValue(dirtyDocument));
        assertNotNull(cleanDocument.body());
        assertEquals("", cleanDocument.body().html());
    }
    
    @Test
    public void testClean_documentWithUnsafeTags() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1><script>console.log('Hello World!');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.hasSameValue(dirtyDocument));
        assertNotNull(cleanDocument.body());
        assertEquals("<h1>Welcome!</h1>", cleanDocument.body().html());
    }
    
    @Test
    public void testIsValid_typicalUseCase_validDocument() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_typicalUseCase_invalidDocument() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1><script>console.log('Hello World!');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_emptyDocument() {
        String dirtyHtml = "";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_documentWithUnsafeTags() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1><script>console.log('Hello World!');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes_typicalUseCase() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        
        assertEquals(0, numDiscarded);
        assertNotNull(cleanDocument.body());
        assertEquals("<h1>Welcome!</h1>", cleanDocument.body().html());
    }
    
    @Test
    public void testCopySafeNodes_documentWithUnsafeTags() {
        String dirtyHtml = "<html><head></head><body><h1>Welcome!</h1><script>console.log('Hello World!');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        
        assertEquals(1, numDiscarded);
        assertNotNull(cleanDocument.body());
        assertEquals("<h1>Welcome!</h1>", cleanDocument.body().html());
    }
    
    @Test
    public void testCreateSafeElement_typicalUseCase() {
        String tagName = "a";
        Whitelist whitelist = Whitelist.relaxed();
        Element sourceEl = new Element(Tag.valueOf(tagName), "");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta.el);
        assertEquals(tagName, elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
    
    @Test
    public void testCreateSafeElement_elementWithUnsafeAttributes() {
        String tagName = "a";
        Whitelist whitelist = Whitelist.relaxed();
        Element sourceEl = new Element(Tag.valueOf(tagName), "");
        sourceEl.attr("href", "javascript:alert('XSS');");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta.el);
        assertEquals(tagName, elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}