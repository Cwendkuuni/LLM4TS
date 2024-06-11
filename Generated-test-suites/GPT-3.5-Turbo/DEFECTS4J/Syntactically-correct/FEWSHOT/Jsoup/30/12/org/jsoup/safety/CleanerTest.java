import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Document dirtyDocument;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        dirtyDocument = new Document("");
        dirtyDocument.appendElement("html")
                .appendElement("body")
                .appendElement("h1").text("Hello World")
                .appendElement("p").text("This is a test");
        
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());
        
        assertTrue(cleanDocument.hasSameValue(dirtyDocument));
    }
    
    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = dirtyDocument.select("body").first();
        Element destination = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
        assertTrue(destination.hasSameValue(root));
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.select("h1").first();
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}