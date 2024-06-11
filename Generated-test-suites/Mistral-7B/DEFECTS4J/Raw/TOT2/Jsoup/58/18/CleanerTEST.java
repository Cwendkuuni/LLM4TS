import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CleanerTest {
    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = new Whitelist(); // create a default whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><p id='test'>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p id='test'></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = new Whitelist(); // create a default whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><script>alert('XSS Attack!')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = new Whitelist(); // create a default whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        String validHtml = "<html><body><p id='test'>Hello World</p></body></html>";
        Document validDocument = Jsoup.parse(validHtml);
        boolean isValid = cleaner.isValid(validDocument);

        assertEquals(true, isValid);
    }

    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = new Whitelist(); // create a default whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        String invalidHtml = "<html><body><script>alert('XSS Attack!')</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);
        boolean isValid = cleaner.isValid(invalidDocument);

        assertEquals(false, isValid);
    }
}