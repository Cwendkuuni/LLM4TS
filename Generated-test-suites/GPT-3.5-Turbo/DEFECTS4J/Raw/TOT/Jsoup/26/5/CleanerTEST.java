import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setup() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void clean_validDirtyDocument_returnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertNotNull(cleanedDocument);
        Assert.assertEquals(dirtyDocument.baseUri(), cleanedDocument.baseUri());
        Assert.assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", cleanedDocument.outerHtml());
        Assert.assertNotSame(cleanedDocument, dirtyDocument);
    }

    @Test
    public void clean_nullDirtyDocument_throwsException() {
        // Arrange
        Document dirtyDocument = null;

        // Act/assert
        Assert.assertThrows(NullPointerException.class, () -> cleaner.clean(dirtyDocument));
    }

    @Test
    public void isValid_validDirtyDocument_returnsTrue() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><h1>Test</h1><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void isValid_invalidDirtyDocument_returnsFalse() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><h1>Test</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_nullDirtyDocument_throwsException() {
        // Arrange
        Document dirtyDocument = null;

        // Act/assert
        Assert.assertThrows(NullPointerException.class, () -> cleaner.isValid(dirtyDocument));
    }

    @Test
    public void copySafeNodes_validSourceAndDestination_copiesSafeNodes() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><h1>Test</h1><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.valueOf("div"), "");

        // Act
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assert
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div><h1>Test</h1><p>Hello, World!</p></div>", dest.outerHtml());
    }

    @Test
    public void copySafeNodes_validSourceWithUnsafeChild_copiesSafeNodesAndDiscardsUnsafeChild() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><h1>Test</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element source = dirtyDocument.select("body").first();
        Element dest = new Element(Tag.valueOf("div"), "");

        // Act
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assert
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals("<div><h1>Test</h1></div>", dest.outerHtml());
    }

    @Test
    public void copySafeNodes_nullSourceElement_returnsZeroDiscarded() {
        // Arrange
        Element source = null;
        Element dest = new Element(Tag.valueOf("div"), "");

        // Act
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assert
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div></div>", dest.outerHtml());
    }

    @Test
    public void createSafeElement_validSourceElement_createsSafeElementWithSafeAttributes() {
        // Arrange
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "https://example.com");
        sourceEl.attr("target", "_blank");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Assert
        Assert.assertNotNull(elementMeta.el);
        Assert.assertEquals("a", elementMeta.el.tagName());
        Assert.assertEquals("https://example.com", elementMeta.el.attr("href"));
        Assert.assertEquals("_blank", elementMeta.el.attr("target"));
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void createSafeElement_validSourceElementWithUnsafeAttributes_createsSafeElementWithoutUnsafeAttributes() {
        // Arrange
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "javascript:alert('XSS')");
        sourceEl.attr("target", "_blank");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Assert
        Assert.assertNotNull(elementMeta.el);
        Assert.assertEquals("a", elementMeta.el.tagName());
        Assert.assertEquals("_blank", elementMeta.el.attr("target"));
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}