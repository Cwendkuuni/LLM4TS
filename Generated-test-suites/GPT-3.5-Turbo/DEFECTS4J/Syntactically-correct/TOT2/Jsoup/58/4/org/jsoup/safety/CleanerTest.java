import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.body().appendChild(body);

        // Test clean method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that cleanDocument only contains elements allowed by the whitelist
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("body", cleanDocument.body().child(0).tagName());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        // Create a dirty document
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.body().appendChild(body);

        // Test isValid method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert that the dirty document is valid
        assertTrue(isValid);
    }

    @Test
    public void testCleanEdgeCases() {
        // Edge case: test with null input
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(null));

        // Edge case: test with empty body
        Document dirtyDocument = new Document("");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(0, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValidEdgeCases() {
        // Edge case: test with null input
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));

        // Edge case: test with invalid content in the body
        Document dirtyDocument = new Document("");
        Element invalidElement = new Element("script", "");
        dirtyDocument.body().appendChild(invalidElement);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

}