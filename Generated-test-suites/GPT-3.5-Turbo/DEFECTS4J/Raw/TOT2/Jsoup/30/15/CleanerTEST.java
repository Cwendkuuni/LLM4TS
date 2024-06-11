import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDoc = new Document("<div><script>alert('XSS');</script></div>");
        Document cleanDoc = cleaner.clean(dirtyDoc);
        assertEquals("<div></div>", cleanDoc.outerHtml());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDoc = new Document("<div><img src='image.png'></div>");
        assertTrue(cleaner.isValid(dirtyDoc));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDoc = new Document("<div><script>alert('XSS');</script></div>");
        Document cleanDoc = cleaner.clean(dirtyDoc);
        int numNodes = cleaner.copySafeNodes(dirtyDoc.body(), cleanDoc.body());
        assertEquals(2, numNodes);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDoc = new Document("<div><script>alert('XSS');</script></div>");
        Document cleanDoc = cleaner.clean(dirtyDoc);
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDoc.body());
        assertNotNull(elementMeta.el);
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}