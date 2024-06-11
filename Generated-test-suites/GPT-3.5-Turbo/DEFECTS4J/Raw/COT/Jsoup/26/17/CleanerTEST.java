import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCleanerWithNullWhitelist() {
        new Cleaner(null);
    }

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertNotNull(cleaner);
    }
    
    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p").addAttributes("p", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertNull(cleaner.clean(null));
    }

    @Test
    public void testCleanWithEmptyBodyDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = new Document("");
        assertNull(cleaner.clean(dirtyDocument));
    }

    @Test
    public void testCleanWithValidBodyDocument() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p").addAttributes("p", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><p class='text'>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p class=\"text\">Hello World!</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValidWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidWithEmptyBodyDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = new Document("");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithValidBodyDocument() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("p").addAttributes("p", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><p class='text'>Hello World!</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    // Add more test cases for edge cases and exception handling scenarios

    // 
    // All the test cases go here
    // 
}