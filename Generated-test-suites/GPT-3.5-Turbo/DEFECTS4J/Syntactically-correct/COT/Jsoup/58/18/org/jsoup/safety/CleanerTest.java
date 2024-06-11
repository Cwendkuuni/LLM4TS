
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Arrange
        String dirtyHtml = "<html><body><div>Hello, World!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertTrue(cleanDocument.body().hasText());
        assertEquals("Hello, World!", cleanDocument.body().text());
    }

    @Test
    public void testIsValid_ValidDocument() {
        // Arrange
        String validHtml = "<html><body><div>Hello, World!</div></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        // Act
        boolean isValid = cleaner.isValid(validDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        // Arrange
        String invalidHtml = "<html><head></head><body><script>alert('XSS');</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        // Act
        boolean isValid = cleaner.isValid(invalidDocument);

        // Assert
        assertTrue(!isValid);
    }
}
