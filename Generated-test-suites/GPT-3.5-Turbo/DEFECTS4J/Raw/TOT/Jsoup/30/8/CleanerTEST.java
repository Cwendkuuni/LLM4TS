package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void clean_shouldReturnCleanedDocument_withValidDirtyDocument() {
        // Arrange
        String dirtyHtml = "<html><body><div><p>This is a test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertTrue(cleanedDocument.hasText());
        assertEquals("<html><head></head><body><div><p>This is a test</p></div></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void clean_shouldReturnCleanedDocument_withNullDirtyDocument() {
        // Arrange
        Document dirtyDocument = null;

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertNotNull(cleanedDocument);
        assertFalse(cleanedDocument.hasText());
        assertEquals("<html><head></head><body></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void isValid_shouldReturnTrue_withValidDirtyDocument() {
        // Arrange
        String dirtyHtml = "<html><body><div><p>This is a test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void isValid_shouldReturnFalse_withInvalidDirtyDocument() {
        // Arrange
        String dirtyHtml = "<html><body><div><p onclick=\"alert('XSS')\">This is a test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void isValid_shouldReturnTrue_withNullDirtyDocument() {
        // Arrange
        Document dirtyDocument = null;

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void copySafeNodes_shouldCopySafeElementsAndTextNodes_withValidRootAndDestination() {
        // Arrange
        String html = "<html><body><div><p>This is a test</p></div></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();
        Element destination = Jsoup.parse("").body();

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        Elements destinationElements = destination.select("*");
        assertEquals(2, destinationElements.size());
        assertEquals(1, destinationElements.select("div").size());
        assertEquals(1, destinationElements.select("p").size());
        assertEquals("This is a test", destinationElements.select("p").first().text());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void copySafeNodes_shouldReturnNumDiscarded_withInvalidRootAndValidDestination() {
        // Arrange
        String html = "<html><body><div><p onclick=\"alert('XSS')\">This is a test</p></div></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();
        Element destination = Jsoup.parse("").body();

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        Elements destinationElements = destination.select("*");
        assertEquals(0, destinationElements.size());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void copySafeNodes_shouldReturnNumDiscarded_withValidRootAndNullDestination() {
        // Arrange
        String html = "<html><body><div><p>This is a test</p></div></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();
        Element destination = null;

        // Act
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        assertEquals(0, numDiscarded);
    }

    @Test
    public void createSafeElement_shouldReturnSafeElement_withValidSourceElement() {
        // Arrange
        String html = "<p>This is a test <span>element</span></p>";
        Document document = Jsoup.parse(html);
        Element sourceElement = document.select("p").first();

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        Element safeElement = elementMeta.el;
        assertEquals("p", safeElement.tagName());
        assertEquals("This is a test element", safeElement.text());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void createSafeElement_shouldReturnSafeElement_withInvalidSourceElement() {
        // Arrange
        String html = "<p onclick=\"alert('XSS')\">This is a test</p>";
        Document document = Jsoup.parse(html);
        Element sourceElement = document.select("p").first();

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        Element safeElement = elementMeta.el;
        assertEquals("p", safeElement.tagName());
        assertEquals("This is a test", safeElement.text());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}