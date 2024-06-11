package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.text().isEmpty());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.simpleTags();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Hello World!", cleanDocument.body().text());
        assertEquals("p", cleanDocument.body().tagName());
    }

    @Test
    public void testCleanerWithCustomWhitelist() {
        Whitelist whitelist = Whitelist.relaxedModel().addTags("div", "span");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='test'><script>alert('XSS')</script>Hello World!<span>Test</span></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Hello World!Test", cleanDocument.body().text());
        assertTrue(cleanDocument.body().tagName().equals("div") || cleanDocument.body().tagName().equals("span"));
    }

    @Test
    public void testIsValidWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.simpleTags();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World!</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithCustomWhitelist() {
        Whitelist whitelist = Whitelist.relaxedModel().addTags("div", "span");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='test'><script>alert('XSS')</script>Hello World!<span>Test</span></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Element cleanElement = cleaner.clean(dirtyDocument).body();

        assertTrue(cleanElement.childNodeSize() == 0);
    }

    @Test
    public void testCopySafeNodesWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.simpleTags();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World!</p></body></html>");
        Element cleanElement = cleaner.clean(dirtyDocument).body();

        assertEquals(1, cleanElement.childNodeSize());
        assertTrue(cleanElement.child(0) instanceof TextNode);
        assertEquals("Hello World!", cleanElement.child(0).text());
    }

    @Test
    public void testCopySafeNodesWithCustomWhitelist() {
        Whitelist whitelist = Whitelist.relaxedModel().addTags("div", "span");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='test'><p>Hello World!</p><script>alert('XSS')</script><span>Test</span></div></body></html>");
        Element cleanElement = cleaner.clean(dirtyDocument).body();

        assertEquals(3, cleanElement.childNodeSize());
        assertTrue(cleanElement.child(0) instanceof TextNode);
        assertEquals("Hello World!", cleanElement.child(0).text());
        assertTrue(cleanElement.child(1) instanceof Element);
        assertEquals("div", cleanElement.child(1).tagName());
        assertTrue(cleanElement.child(2) instanceof TextNode);
        assertEquals("Test", cleanElement.child(2).text());
    }
}