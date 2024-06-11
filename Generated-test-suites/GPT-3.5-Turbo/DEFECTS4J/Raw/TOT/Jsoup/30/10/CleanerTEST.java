import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean_WithValidDocument_ReturnsCleanedDocument() {
        // Arrange
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertNotNull(cleanedDocument);
        assertEquals(html, cleanedDocument.html());
    }

    @Test
    public void testClean_WithNullDocument_ThrowsException() {
        // Arrange
        Document dirtyDocument = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(dirtyDocument));
    }

    @Test
    public void testIsValid_WithValidDocument_ReturnsTrue() {
        // Arrange
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_WithInvalidDocument_ReturnsFalse() {
        // Arrange
        String html = "<script>alert('XSS')</script>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testCreateSafeElement_WithSafeAttribute_ReturnsElementWithAttribute() {
        // Arrange
        String tagName = "a";
        Element sourceEl = new Element(tagName);

        // Act
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Assert
        assertNotNull(elementMeta.el);
        assertEquals(tagName, elementMeta.el.tagName());
    }

    @Test
    public void testCreateSafeElement_WithUnsafeAttribute_DiscardsAttribute() {
        // Arrange
        String tagName = "a";
        Element sourceEl = new Element(tagName);
        sourceEl.attr("onclick", "alert('XSS')");

        // Act
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Assert
        assertNotNull(elementMeta.el);
        assertEquals(tagName, elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCopySafeNodes_WithSafeTags_CopiesAllSafeNodes() {
        // Arrange
        String html = "<html><body><p>Hello, World!</p><div><a href=\"#\">Link</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element root = dirtyDocument.body();
        Element destination = new Element("body");

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        assertEquals(0, numDiscarded);
        assertEquals(html, destination.html());
    }

    @Test
    public void testCopySafeNodes_WithUnsafeTags_DiscardsUnsafeTags() {
        // Arrange
        String html = "<html><body><script>alert('XSS')</script><div><a href=\"#\">Link</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element root = dirtyDocument.body();
        Element destination = new Element("body");

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        assertEquals(1, numDiscarded);
        assertEquals("<div><a href=\"#\">Link</a></div>", destination.html());
    }

    @Test
    public void testCopySafeNodes_WithTextNodes_CopiesAllTextNodes() {
        // Arrange
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element root = dirtyDocument.body();
        Element destination = new Element("body");

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        assertEquals(0, numDiscarded);
        assertEquals(html, destination.html());
    }
}