import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        // Given
        Whitelist whitelist = Whitelist.simpleTagNames("p", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><body><p id='test'>Hello World</p><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
        assertEquals("<html><body><p id='test'>Hello World</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        // Given
        Whitelist whitelist = Whitelist.simpleTagNames("p", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><body><p id='test'>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertTrue(isValid);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanMethodWithNullDocument() {
        // Given
        Whitelist whitelist = Whitelist.simpleTagNames("p", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        // When & Then
        cleaner.clean(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMethodWithNullDocument() {
        // Given
        Whitelist whitelist = Whitelist.simpleTagNames("p", "h1", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        // When & Then
        cleaner.isValid(null);
    }
}