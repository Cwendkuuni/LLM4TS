import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://example.com");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta);
        assertEquals("a", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}