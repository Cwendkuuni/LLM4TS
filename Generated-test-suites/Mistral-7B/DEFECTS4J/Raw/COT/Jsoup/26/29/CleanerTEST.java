package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.whitelist.Elements;
import org.jsoup.whitelist.Whitelist;

import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    @org.junit.Before
    public void setUp() {
        Whitelist whitelist = Whitelist.simpleText();
        cleaner = new Cleaner(whitelist);
    }

    private Cleaner cleaner;

    /**
     * Test method for {@link Cleaner#Cleaner(Whitelist)}
     */
    @org.junit.Test
    public void testCleaner() {
        assertNotNull(cleaner);
    }

    /**
     * Test method for {@link Cleaner#clean(Document)}
     */
    @org.junit.Test
    public void testClean() {
        String dirtyHtml = "<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        assertEquals(Elements.h1(), root.tag());
        assertNull(root.select("script").first());
    }

    /**
     * Test method for {@link Cleaner#isValid(Document)}
     */
    @org.junit.Test
    public void testIsValid() {
        String dirtyHtml = "<html><body><h1 id='test'>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        dirtyHtml = "<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>";
        invalidateDocument(dirtyDocument);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        dirtyHtml = "<html><body><h1 id='test' onclick='alert(\"XSS\")'>Hello</h1></body></html>";
        invalidateDocument(Jsoup.parseBodyFragment(dirtyHtml));
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    /**
     * Test method for {@link Cleaner#copySafeNodes(Element, Element)}
     */
    @org.junit.Test
    public void testCopySafeNodes() {
        String dirtyHtml = "<html><body><script>alert('XSS')</script><h1 id='test'>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        Element cleanRoot = new Element(Tag.BODY, dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanRoot);

        assertEquals(1, numDiscarded);
        assertNull(cleanRoot.select("script").first());
        assertEquals("Hello", cleanRoot.select("h1").text());
    }

    /**
     * Test method for {@link Cleaner#createSafeElement(Element)}
     */
    @org.junit.Test
    public void testCreateSafeElement() {
        String dirtyHtml = "<html><body><h1 id='test' onclick='alert(\"XSS\")'>Hello</h1></body></html>";
        Element dirtyElement = ElementFilter.head(Jsoup.parseBodyFragment(dirtyHtml).body());

        ElementMeta meta = cleaner.createSafeElement(dirtyElement);

        assertEquals(dirtyElement.tagName(), meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    private void invalidateDocument(Document document) {
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.outputSettings().preserveWhitespace(true);
        document.outputSettings().charset("UTF-8");
        document.outputSettings().xmlDeclaration(true);
        document.outputSettings().prettyPrint(true);

        elementWithScript = new Element("script", document.baseUri());
        elementWithScript.attr("src", "malicious.js");
        document.body().appendChild(elementWithScript);
    }

    private Element elementWithScript;
}