import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.assertEquals;

public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        // Arrange
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(" <html><body><h1>Hello World</h1></body></html>");

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidWithValidDocument() {
        // Arrange
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(" <html><body><h1>Hello World</h1></body></html>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }
}