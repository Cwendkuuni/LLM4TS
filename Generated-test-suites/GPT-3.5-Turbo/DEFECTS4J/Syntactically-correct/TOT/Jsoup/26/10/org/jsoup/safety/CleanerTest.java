import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean_validDirtyDocument_success() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<div>Test</div>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertNotNull(cleanedDocument);
        assertEquals(1, cleanedDocument.body().children().size());
        assertEquals("div", cleanedDocument.body().child(0).tagName());
        assertEquals("Test", cleanedDocument.body().child(0).text());
    }

    @Test
    public void testClean_nullDirtyDocument_throwException() {
        // Arrange
        Document dirtyDocument = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(dirtyDocument));
    }

    @Test
    public void testIsValid_validDirtyDocument_returnTrue() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDirtyDocument_returnFalse() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_nullDirtyDocument_throwException() {
        // Arrange
        Document dirtyDocument = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes_validSource_destContainsSafeElements() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Test</p></div>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        // Act
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Assert
        assertEquals(0, numDiscarded);
        assertTrue(cleanDocument.body().children().size() > 0);
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("p", cleanDocument.body().child(0).child(0).tagName());
        assertEquals("Test", cleanDocument.body().child(0).child(0).text());
    }

    @Test
    public void testCopySafeNodes_invalidSource_destDoesNotContainUnsafeElements() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS')</script></div>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        // Act
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Assert
        assertEquals(1, numDiscarded);
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testCopySafeNodes_nullSource_throwException() {
        // Arrange
        Element source = null;
        Document dest = Document.createShell("http://example.com");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cleaner.copySafeNodes(source, dest.body()));
    }

    @Test
    public void testCreateSafeElement_validSource_returnSafeElement() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("div"), "http://example.com");
        sourceElement.attr("data-test", "test");
        sourceElement.attr("class", "test");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;

        // Assert
        assertNotNull(destElement);
        assertEquals("div", destElement.tagName());
        assertEquals("http://example.com", destElement.baseUri());
        assertNotNull(destElement.attributes().get("data-test"));
        assertNotNull(destElement.attributes().get("class"));
        assertNull(destElement.attributes().get("invalid-attr"));
    }

    @Test
    public void testCreateSafeElement_invalidSource_returnSafeElementWithDiscardedAttributes() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("script"), "http://example.com");
        sourceElement.attr("src", "http://example.com/malicious.js");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;

        // Assert
        assertNotNull(destElement);
        assertEquals("script", destElement.tagName());
        assertEquals("http://example.com", destElement.baseUri());
        assertTrue(destElement.attributes().isEmpty());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}