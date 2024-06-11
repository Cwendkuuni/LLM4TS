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
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div>HelloWorld</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div>HelloWorld</div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><head><script>alert('XSS')</script></head><body><div>HelloWorld</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = new Document("<html><body><div>HelloWorld</div></body></html>");
        Document cleanDocument = new Document("<html><body></body></html>");
        
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        
        assertEquals(0, numDiscarded);
        assertEquals("<html><body><div>HelloWorld</div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta elementMeta = cleaner.createSafeElement(new Element("div", ""));
        
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}