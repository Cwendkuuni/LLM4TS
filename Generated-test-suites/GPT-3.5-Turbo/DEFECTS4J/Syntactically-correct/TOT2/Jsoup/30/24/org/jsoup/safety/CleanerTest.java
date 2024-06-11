import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
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
        Document dirtyDocument = new Document("<html><body><div>Hello World</div></body></html>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanedDocument);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", cleanedDocument.html());
    }

    @Test
    public void testIsValid() {
        Document validDocument = new Document("<html><body><p>Hello World</p></body></html>");
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element("div");
        Element destination = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
        assertEquals(0, destination.childNodeSize());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("a");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}