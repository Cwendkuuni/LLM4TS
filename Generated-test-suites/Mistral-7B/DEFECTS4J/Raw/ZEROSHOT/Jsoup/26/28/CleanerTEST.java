package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist(); // Assuming a valid Whitelist implementation
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Document.createFromString("<html><body>" +
                "<h1 id='test' onload='alert(\"XSS\")'>Hello World</h1>" +
                "<script src='malicious.js'></script>" +
                "</body></html>", "http://test.com");
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p></p></body></html>", cleanDocument.body().html()); // Check if all unwanted elements are removed
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = Document.createFromString("<html><body>" +
                "<h1 id='test'>Hello World</h1>" +
                "</body></html>", "http://test.com");
        Cleaner cleaner = new Cleaner(new Whitelist());

        // Test valid document
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test invalid document
        Document invalidDocument = Document.createFromString("<html><body>" +
                "<h1 id='test' onload='alert(\"XSS\")'>Hello World</h1>" +
                "</body></html>", "http://test.com");
        assertFalse(cleaner.isValid(invalidDocument));

        // Test valid document after cleaning
        Document cleanValidDocument = cleaner.clean(invalidDocument);
        assertTrue(cleaner.isValid(cleanValidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Document.createFromString("<html><body>" +
                "<h1 id='test'>Hello <script>alert('XSS')</script> World</h1>" +
                "</body></html>", "http://test.com");
        Cleaner cleaner = new Cleaner(new Whitelist());

        // Test with valid document
        Document cleanDocument = Document.createShell("http://test.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
        assertEquals("<html><body></body></html>", cleanDocument.body().html());

        // Test with invalid document
        Document invalidDocument = Document.createShell("http://test.com");
        numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), invalidDocument.body());
        assertEquals(1, numDiscarded); // Number of script tag discarded
        assertEquals("<html><body></body></html>", invalidDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Element dirtyElement = new Element(Tag.valueOf("script"), "http://test.com");
        dirtyElement.attributes().put("src", "malicious.js");
        Cleaner cleaner = new Cleaner(new Whitelist());

        ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);

        assertNotNull(elementMeta.el); // Check if a new safe element is created
        assertEquals(1, elementMeta.numAttribsDiscarded); // Check if the 'src' attribute is discarded
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null); // Should throw a NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testCleanDocumentNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = null;
        Document cleanDocument = cleaner.clean(dirtyDocument); // Should throw a NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidDocumentNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = null;
        boolean isValid = cleaner.isValid(dirtyDocument); // Should throw a NullPointerException
    }
}