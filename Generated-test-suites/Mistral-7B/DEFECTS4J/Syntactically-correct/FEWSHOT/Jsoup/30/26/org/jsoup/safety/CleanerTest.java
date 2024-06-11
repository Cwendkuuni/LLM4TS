package org.jsoup.safety;

import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.s;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = s("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().toString().isEmpty());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Document dirtyDocument = s("<html><body><p>Hello World</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p>Hello World</p>", cleanDocument.body().toString());
    }

    @Test
    public void testCleanerWithCustomWhitelist() {
        Whitelist customWhitelist = Whitelist.simpleTextAndTags("p", "strong");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = s("<html><body><p>Hello <strong>World</strong></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p>Hello <strong>World</strong></p>", cleanDocument.body().toString());
    }

    @Test
    public void testIsValidWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = s("<html><body></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Document dirtyDocument = s("<html><body><p>Hello World</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithCustomWhitelist() {
        Whitelist customWhitelist = Whitelist.simpleTextAndTags("p", "strong");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = s("<html><body><p>Hello World</p><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodesWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Element root = new Element("html");
        Element body = new Element("body");
        root.appendChild(body);

        Element script = new Element("script");
        script.text("alert('XSS')");
        body.appendChild(script);

        int numDiscarded = cleaner.copySafeNodes(root, body);

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Element root = new Element("html");
        Element body = new Element("body");
        root.appendChild(body);

        Element p = new Element("p");
        p.text("Hello World");
        body.appendChild(p);

        int numDiscarded = cleaner.copySafeNodes(root, body);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Element sourceEl = new Element("p");
        sourceEl.text("Hello World");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNull(meta.el);
    }

    @Test
    public void testCreateSafeElementWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Element sourceEl = new Element("p");
        sourceEl.text("Hello World");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertTrue(meta.el.toString().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithCustomWhitelist() {
        Whitelist customWhitelist = Whitelist.simpleTextAndTags("p", "strong");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Element sourceEl = new Element("p");
        sourceEl.text("Hello World");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertTrue(meta.el.toString().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithScriptTag() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Element sourceEl = new Element("script");
        sourceEl.text("alert('XSS')");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNull(meta.el);
    }

    @Test
    public void testCreateSafeElementWithInvalidAttribute() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Element sourceEl = new Element("p");
        sourceEl.attr("xss", "script:alert('XSS')");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNull(meta.el);
    }

    @Test
    public void testCreateSafeElementWithValidAttribute() {
        Whitelist basicWhitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Element sourceEl = new Element("p");
        sourceEl.attr("class", "test");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("class", meta.el.tagName());
        assertEquals("class", meta.el.getAttribute("class"));
        assertTrue(meta.el.toString().isEmpty());
    }

}