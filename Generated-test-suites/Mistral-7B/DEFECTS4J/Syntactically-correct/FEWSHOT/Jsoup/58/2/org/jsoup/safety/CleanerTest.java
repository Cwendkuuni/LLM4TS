package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleaner_ValidInput() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleaner_InvalidInput() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_ValidInput() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidInput() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitor() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element root = dirtyDocument.body();
        Element destination = new Element("div");

        // When
        CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        // Then
        assertEquals(1, destination.childNodes().size()); // The p tag
        assertNull(destination.tagName()); // The div tag should not have a tag name
    }
}