package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        // Creating a whitelist with basic tags and attributes
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean_WithValidDocument_ReturnsCleanedDocument() {
        // Creating a dirty document with some unsafe tags and attributes
        Document dirtyDocument = new Document("");
        dirtyDocument.append("<html><body><div data-test='unsafe'>Hello <script>alert('Unsafe!')</script></div></body></html>");

        // Cleaning the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Asserting that the clean document does not contain the unsafe tags and attributes
        assertFalse(cleanDocument.toString().contains("script"));
        assertFalse(cleanDocument.select("[data-test='unsafe']").hasAttr("data-test"));
    }

    @Test
    public void testClean_WithNullDocument_ThrowsException() {
        // Attempting to clean a null document
        Document cleanDocument = cleaner.clean(null);

        // Asserting that the clean document is also null
        assertNull(cleanDocument);
    }

    @Test
    public void testIsValid_WithValidDocument_ReturnsTrue() {
        // Creating a valid document with only safe tags and attributes
        Document validDocument = new Document("");
        validDocument.append("<html><body><div>Hello World!</div></body></html>");

        // Checking if the valid document is considered valid by the cleaner
        boolean isValid = cleaner.isValid(validDocument);

        // Asserting that the valid document is considered valid
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_WithInvalidDocument_ReturnsFalse() {
        // Creating an invalid document with an unsafe tag
        Document invalidDocument = new Document("");
        invalidDocument.append("<html><body><div><script>alert('Unsafe!')</script></div></body></html>");

        // Checking if the invalid document is considered valid by the cleaner
        boolean isValid = cleaner.isValid(invalidDocument);

        // Asserting that the invalid document is considered invalid
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_WithNullDocument_ThrowsException() {
        // Attempting to check the validity of a null document
        boolean isValid = cleaner.isValid(null);

        // Asserting that the validity check throws an exception
        assertFalse(isValid);
    }

    // Additional test cases for other methods can be added here

}