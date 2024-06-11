import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;

public class CleanerTest {
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        // Add more assertions if necessary
    }

    @Test
    public void testCleanMethod() {
        // Test with a valid input document
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());

        // Test with an invalid input document
        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!')</script></body></html>");
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(invalidDocument);
        assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidMethod() {
        // Test with a valid document
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document validDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        boolean result = cleaner.isValid(validDocument);
        assertEquals(true, result);

        // Test with an invalid document
        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!')</script></body></html>");
        result = cleaner.isValid(invalidDocument);
        assertEquals(false, result);
    }
}