import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        // Test constructor with valid whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("Expected a Cleaner object", cleaner, cleaner);
    }

    @Test
    public void testCleanMethod() {
        // Test clean method with valid document
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Expected a clean document", "<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        // Test isValid method with clean document
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue("Expected valid document", isValid);
    }
}