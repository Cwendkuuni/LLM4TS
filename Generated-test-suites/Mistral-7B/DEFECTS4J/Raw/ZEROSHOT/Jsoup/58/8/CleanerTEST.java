package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCreateCleanerWithNullWhitelist() {
        assertNull(new Cleaner(null));
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertEquals(1, body.childNodes().size());
        Element h1 = body.child(0);
        assertEquals("Hello", h1.text());
        assertFalse(cleanDocument.body().hasMetaCharset());
        assertFalse(cleanDocument.body().hasLang());
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document validDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
        assertFalse(cleaner.isValid(validDocument)); // even though it's valid, it's not what the Cleaner is designed for

        Document validWhitelistDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1></body></html>");
        assertTrue(cleaner.isValid(validWhitelistDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1 id='title'>Hello<script>alert('XSS')</script></h1></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Element title = cleanDocument.body().getElementById("title");
        assertEquals("Hello", title.text());
        assertEquals(2, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("div", new Attributes().put("id", "sourceDiv").put("class", "sourceClass"));

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;

        assertEquals("div", safeElement.tagName());
        assertEquals(1, safeElement.childNodes().size());
        TextNode textNode = (TextNode) safeElement.child(0);
        assertEquals("", textNode.text());

        Element idAttribute = safeElement.getElementById("id");
        assertNotNull(idAttribute);
        assertEquals("sourceDiv", idAttribute.attr("id"));

        Elements classAttribute = safeElement.classes();
        assertEquals(1, classAttribute.size());
        assertEquals("sourceClass", classAttribute.get(0));

        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}