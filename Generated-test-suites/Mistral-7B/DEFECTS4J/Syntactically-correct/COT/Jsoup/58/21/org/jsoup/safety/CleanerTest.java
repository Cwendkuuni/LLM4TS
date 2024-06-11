package org.jsoup.safety.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;

import java.io.IOException;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simplest();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() throws IOException {
        Whitelist whitelist = Whitelist.simpleList("p", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='p1'>Hello World</p><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p id='p1'>Hello World</p></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidMethod() throws IOException {
        Whitelist whitelist = Whitelist.simpleList("p", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><p id='p1'>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertTrue(cleaner.isValid(dirtyDocument));

        html = "<html><head><script>alert('XSS');</script></body></html>";
        Document invalidDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(invalidDocument));

        html = "<html><body><p id='p1'>Hello World<script>alert('XSS');</script></p></body></html>";
        Document invalidDocumentWithScriptInsideTag = Jsoup.parse(html);

        assertFalse(cleaner.isValid(invalidDocumentWithScriptInsideTag));

        html = "<html><body><p id='p1'>Hello World<img src='javascript:alert(\"XSS\");'></p></body></html>";
        Document invalidDocumentWithScriptInImage = Jsoup.parse(html);

        assertFalse(cleaner.isValid(invalidDocumentWithScriptInImage));

        html = "<html><body><p id='p1' data-xss='<script>alert(\"XSS\");</script>'>Hello World</p></body></html>";
        Document invalidDocumentWithScriptInAttribute = Jsoup.parse(html);

        assertFalse(cleaner.isValid(invalidDocumentWithScriptInAttribute));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerConstructionWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanMethodWithNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleList("p", "a"));
        cleaner.clean(null);
    }
}