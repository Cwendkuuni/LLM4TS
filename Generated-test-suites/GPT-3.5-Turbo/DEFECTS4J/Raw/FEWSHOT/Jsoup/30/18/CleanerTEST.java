import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><h1>Hello World!</h1></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("<html><head></head><body><div><h1>Hello World!</h1></div></body></html>", cleanDocument.html());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_withSafeDocument() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><h1>Hello World!</h1></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello World!</p></div></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element dirtyElement = dirtyDocument.body();
        Element cleanElement = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
        
        assertEquals(0, numDiscarded);
        assertNotNull(cleanElement);
        assertNotNull(cleanElement.children());
        assertEquals(1, cleanElement.children().size());
        assertEquals("div", cleanElement.children().first().tagName());
        assertEquals("p", cleanElement.children().first().children().first().tagName());
        assertEquals("Hello World!", cleanElement.children().first().children().first().text());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Attributes sourceAttributes = new Attributes();
        sourceAttributes.put("class", "container");
        sourceElement.attributes(sourceAttributes);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertNotNull(elementMeta.el);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(1, elementMeta.el.attributes().size());
        assertEquals("class", elementMeta.el.attributes().get("class").getKey());
        assertEquals("container", elementMeta.el.attributes().get("class").getValue());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}