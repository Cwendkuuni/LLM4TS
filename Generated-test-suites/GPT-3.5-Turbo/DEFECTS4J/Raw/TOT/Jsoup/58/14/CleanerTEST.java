import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {

    @org.junit.Test
    public void testClean_WithDirtyDocument_ReturnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><p>Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.outerHtml());
    }

    @org.junit.Test
    public void testClean_WithEmptyDocument_ReturnsCleanedDocumentWithEmptyBody() {
        // Arrange
        String dirtyHtml = "<html><head><title>Empty Document</title></head><body></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.outerHtml());
    }

    @org.junit.Test
    public void testIsValid_WithValidDocument_ReturnsTrue() {
        // Arrange
        String html = "<html><head><title>Valid Document</title></head><body><p>Test</p></body></html>";
        Document validDocument = Jsoup.parse(html);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Act
        boolean isValid = cleaner.isValid(validDocument);
        
        // Assert
        assertTrue(isValid);
    }

    @org.junit.Test
    public void testIsValid_WithInvalidDocument_ReturnsFalse() {
        // Arrange
        String html = "<html><head><title>Invalid Document</title></head><body><script>alert('XSS');</script></body></html>";
        Document invalidDocument = Jsoup.parse(html);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Act
        boolean isValid = cleaner.isValid(invalidDocument);
        
        // Assert
        assertFalse(isValid);
    }
}