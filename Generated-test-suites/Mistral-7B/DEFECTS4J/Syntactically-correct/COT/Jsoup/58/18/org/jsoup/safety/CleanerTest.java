package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.safety.Cleaner.ElementMeta;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CleanerTest {

    @org.junit.Before
    public void setUp() {
        Whitelist whitelist = Whitelist.simpleText();
        cleaner = new Cleaner(whitelist);
    }

    private Cleaner cleaner;

    @org.junit.Test
    public void testCleanerConstruction() {
        assertNotNull(cleaner);
    }

    @org.junit.Test
    public void testCleanEmptyDocument() throws IOException {
        Document dirtyDocument = Jsoup.parseBodyFragment("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    @org.junit.Test
    public void testCleanDocumentWithAllowedContent() throws IOException {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<p>Hello World</p>", cleanDocument.body().html());
    }

    @org.junit.Test
    public void testCleanDocumentWithDisallowedContent() throws IOException {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.body().html());
        assertFalse(cleanDocument.body().hasText());
    }

    @org.junit.Test
    public void testIsValidDocumentWithAllowedContent() throws IOException, ParseError {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @org.junit.Test
    public void testIsValidDocumentWithDisallowedContent() throws IOException, ParseError {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(html);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @org.junit.Test
    public void testCopySafeNodesWithAllowedContent() throws IOException {
        String html = "<html><body><p>Hello World</p></body></html>";
        Element source = Jsoup.parseBodyFragment(html).body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals("<p>Hello World</p>", dest.html());
    }

    @org.junit.Test
    public void testCopySafeNodesWithDisallowedContent() throws IOException {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Element source = Jsoup.parseBodyFragment(html).body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertFalse(dest.hasText());
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedAttributes() {
        Element sourceEl = new Element("div", null);
        sourceEl.attributes().put("class", "test");
        Element dest = cleaner.createSafeElement(sourceEl);
        ElementMeta elementMeta = (ElementMeta) dest.attributes().get("class");
        assertEquals("test", elementMeta.el.tagName());
        assertEquals("class", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @org.junit.Test
    public void testCreateSafeElementWithDisallowedAttributes() {
        Element sourceEl = new Element("div", null);
        sourceEl.attributes().put("onclick", "alert('XSS')");
        Element dest = cleaner.createSafeElement(sourceEl);
        assertTrue(dest.attributes().isEmpty());
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedTagsAndNestedDisallowedTags() {
        Element sourceEl = new Element("div", null);
        Element nested = new Element("script", null);
        nested.text("alert('XSS')");
        sourceEl.appendChild(nested);
        Element dest = cleaner.createSafeElement(sourceEl);
        assertTrue(dest.children().isEmpty());
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedTagsAndNestedAllowedTags() {
        Element sourceEl = new Element("div", null);
        Element nested = new Element("p", null);
        nested.text("Hello World");
        sourceEl.appendChild(nested);
        Element dest = cleaner.createSafeElement(sourceEl);
        assertEquals("<p>Hello World</p>", dest.html());
    }

    @org.junit.Test
    public void testCreateSafeElementWithAllowedTagsAndNestedDisallowedAttributes() {
        Element sourceEl = new Element("div", null);
        Element nested = new Element("p", null);
        nested.attributes().put("onclick", "alert('XSS')");
        sourceEl.appendChild(nested);
        Element dest = cleaner.createSafeElement(sourceEl);
        ElementMeta elementMeta = (ElementMeta) dest.children().get(0).attributes().get("onclick");
        assertNull(elementMeta);
    }
}