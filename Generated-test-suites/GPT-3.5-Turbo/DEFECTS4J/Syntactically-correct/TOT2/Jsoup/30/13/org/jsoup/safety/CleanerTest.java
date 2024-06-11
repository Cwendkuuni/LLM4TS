import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        // Add more assertions as needed
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
        // Add more assertions as needed
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
        // Add more assertions as needed
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());
        assertNotNull(elementMeta);
        // Add more assertions as needed
    }

}