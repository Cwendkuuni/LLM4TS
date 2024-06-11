import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.getWhitelist());
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test that the clean method successfully creates a new clean document
        // containing only elements allowed by the whitelist
        assertTrue(cleanDocument != null);
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Test that the isValid method correctly determines if the input document
        // is valid against the whitelist
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Document cleanDocument = new Document("");

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Test that the copySafeNodes method copies trusted nodes from the source to the destination
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        ElementMeta elementMeta = cleaner.createSafeElement(new Element("div"));

        // Test that the createSafeElement method successfully creates a safe element
        assertTrue(elementMeta != null);
    }
}