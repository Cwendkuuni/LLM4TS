package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic()); // Use basic whitelist for testing
    }

    @Test
    public void testClean_validInputDocument_returnsCleanDocument() {
        // Arrange
        String dirtyHtml = "<div><p>Hello, <b>world!</b></p></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertEquals("<html><head></head><body><div><p>Hello, <b>world!</b></p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_dirtyInputDocument_returnsCleanedBody() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><div><p>Hello, <b>world!</b></p><script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertEquals("<div><p>Hello, <b>world!</b></p></div>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid_validInputDocument_returnsTrue() {
        // Arrange
        String validHtml = "<div><p>Hello, <b>world!</b></p></div>";
        Document validDocument = Jsoup.parse(validHtml);

        // Act
        boolean isValid = cleaner.isValid(validDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidInputDocument_returnsFalse() {
        // Arrange
        String invalidHtml = "<html><head></head><body><div><p>Hello, <b>world!</b></p><script>alert('XSS');</script></div></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        // Act
        boolean isValid = cleaner.isValid(invalidDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_validInputDocument_returnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<div><p>Hello, <b>world!</b></p><script>alert('XSS');</script></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        // Act
        int discardedElements = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Assert
        Assert.assertEquals("<div><p>Hello, <b>world!</b></p></div>", cleanDocument.body().html());
        Assert.assertEquals(1, discardedElements);
    }

    @Test
    public void testCreateSafeElement_withSafeAttribute_returnsSafeElement() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        sourceElement.attr("data-safe", "true");

        // Act
        ElementMeta meta = cleaner.createSafeElement(sourceElement);

        // Assert
        Assert.assertEquals("<div data-safe=\"true\"></div>", meta.el.outerHtml());
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_withUnsafeAttribute_returnsSafeElementAndDiscardsAttribute() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        sourceElement.attr("onclick", "alert('XSS')");

        // Act
        ElementMeta meta = cleaner.createSafeElement(sourceElement);

        // Assert
        Assert.assertEquals("<div></div>", meta.el.outerHtml());
        Assert.assertEquals(1, meta.numAttribsDiscarded);
    }

}