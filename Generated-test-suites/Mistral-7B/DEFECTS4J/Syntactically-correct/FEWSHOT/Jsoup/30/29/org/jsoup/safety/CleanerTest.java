package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><div id='test'></div></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesEmptyElement() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body></body></html>");
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.BODY);
        int numDiscarded = Cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesSimpleElement() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><p id='test'>Test</p></body></html>");
        Element root = dirtyDocument.body().getElementsByTag("p").first();
        Element destination = new Element(Tag.BODY);
        int numDiscarded = Cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesInvalidTag() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><script>alert('XSS')</script></body></html>");
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.BODY);
        int numDiscarded = Cleaner.copySafeNodes(root, destination);
        assertTrue(numDiscarded > 0);
    }

    @Test
    public void testCopySafeNodesInvalidAttribute() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is instantiated correctly
        Document dirtyDocument = Document.parse("<html><body><p id='test' onclick='alert('XSS')'>Test</p></body></html>");
        Element root = dirtyDocument.body().getElementsByTag("p").first();
        Element destination = new Element(Tag.BODY);
        int numDiscarded = Cleaner.copySafeNodes(root, destination);
        assertTrue(numDiscarded > 0);
    }
}