package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist validWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(validWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <b>world</b></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>Hello <b>world</b></p></body></html>", cleanDocument.toString());
    }

    @Test
    public void testCleanerWithInvalidWhitelist() {
        Whitelist invalidWhitelist = Whitelist.simpleText();
        invalidWhitelist.addTag("script", "*");
        Cleaner cleaner = new Cleaner(invalidWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <script>alert('XSS');</script> world</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().childNodes().isEmpty());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Whitelist validWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(validWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <b>world</b></p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Whitelist invalidWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(invalidWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <script>alert('XSS');</script> world</p></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithValidDocument() {
        Whitelist validWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(validWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <b>world</b></p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithInvalidDocument() {
        Whitelist invalidWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(invalidWhitelist);

        Document dirtyDocument = Document.parse("<html><body><p>Hello <script>alert('XSS');</script> world</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertTrue(cleanDocument.body().childNodes().isEmpty());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithValidElement() {
        Whitelist validWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(validWhitelist);

        Element sourceElement = new Element("p", null, new Attributes().put("class", "test"));
        ElementMeta destElementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("<p class=\"test\"></p>", destElementMeta.el.toString());
        assertEquals(0, destElementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithInvalidElement() {
        Whitelist invalidWhitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(invalidWhitelist);

        Element sourceElement = new Element("script", null, new Attributes().put("src", "evil.js"));
        ElementMeta destElementMeta = cleaner.createSafeElement(sourceElement);

        assertTrue(destElementMeta.el == null);
        assertEquals(1, destElementMeta.numAttribsDiscarded);
    }
}