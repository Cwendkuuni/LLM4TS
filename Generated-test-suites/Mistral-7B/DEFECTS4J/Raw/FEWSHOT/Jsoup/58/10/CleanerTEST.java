package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class CleanerTest {

    private static final Whitelist WHITELIST = Whitelist.simpleDefault();

    @Test
    public void testCleanerWithEmptyDocument() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Cleaner cleaner = new Cleaner(null);
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><body><p>Text</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><body><p>Text <b>bold</b> <a href=\"http://example.com\">link</a></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element pElement = cleanDocument.body().get(0);
        assertEquals("Text <b></b> <a></a>", pElement.html());
    }

    @Test
    public void testCleanerWithInvalidDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS');</script>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testCleanerWithScriptInAttribute() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<div id=\"test\" onclick=\"alert('XSS');\"></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element divElement = cleanDocument.selectFirst("#test");
        assertNull(divElement.attr("onclick"));
    }

    @Test
    public void testCleanerWithScriptInStyleAttribute() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<div id=\"test\" style=\"color: red;background-color: url(javascript:alert('XSS'));\"></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element divElement = cleanDocument.selectFirst("#test");
        assertEquals("", divElement.attr("style"));
    }

    @Test
    public void testCleanerWithEntityReferences() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>&lt;script&gt;alert('XSS');&lt;/script&gt;</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p></p>", cleanDocument.body().html());
    }

    @Test
    public void testCleanerWithUnsafeInput() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Text &lt;script&gt;alert('XSS');&lt;/script&gt;</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p></p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidWithEmptyDocument() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithValidDocument() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><body><p>Text</p></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS');</script>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithDocumentWithScriptInAttribute() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div id=\"test\" onclick=\"alert('XSS');\"></div>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithDocumentWithScriptInStyleAttribute() {
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div id=\"test\" style=\"color: red;background-color: url(javascript:alert('XSS'));\"></div>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanerWithParseError() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Parser parser = new Parser();
        Document dirtyDocument = parser.parseBodyFragment("<script>alert('XSS');</script>");

        try {
            cleaner.clean(dirtyDocument);
            fail("Expected ParseError");
        } catch (ParseError e) {
            // This is expected
        }
    }
}