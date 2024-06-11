import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void clean_ValidDirtyDocument_ReturnsCleanedDocument() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<div><p>Hello, World!</p></div>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", cleanedDocument.toString());
    }

    @Test
    public void clean_DirtyDocumentWithUnsafeTags_RemovesUnsafeTags() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<div><script>alert('XSS');</script></div>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html><head></head><body><div></div></body></html>", cleanedDocument.toString());
    }

    @Test
    public void clean_DirtyDocumentWithUnsafeAttributes_RemovesUnsafeAttributes() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<div class=\"unsafe\" style=\"color:red;\">Hello, World!</div>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html><head></head><body><div>Hello, World!</div></body></html>", cleanedDocument.toString());
    }

    @Test
    public void clean_DirtyDocumentWithEnforcedAttributes_AddsEnforcedAttributes() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<a href=\"http://example.com\">Link</a>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html><head></head><body><a href=\"http://example.com\" rel=\"nofollow\">Link</a></body></html>", cleanedDocument.toString());
    }

    @Test
    public void isValid_ValidDirtyDocument_ReturnsTrue() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<p>Hello, World!</p>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void isValid_DirtyDocumentWithUnsafeTags_ReturnsFalse() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<script>alert('XSS');</script>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(!isValid);
    }

    @Test
    public void isValid_DirtyDocumentWithUnsafeAttributes_ReturnsFalse() {
        // Arrange
        Document dirtyDocument = Parser.parseBodyFragment("<div class=\"unsafe\" style=\"color:red;\">Hello, World!</div>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(!isValid);
    }  

}