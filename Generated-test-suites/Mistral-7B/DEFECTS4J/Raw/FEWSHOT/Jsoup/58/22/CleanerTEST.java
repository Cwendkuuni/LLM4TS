package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.helper.DataStructures;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class CleanerTest {

    private static final Whitelist DEFAULT_WHITELIST = Whitelist.simpleTextOnlyWhitelist();

    @Test
    public void testCreateCleaner() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        String html = "<html><head></head><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p>Test</p></div></body>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithInvalidDocument() {
        String html = "<html><head><script>alert('xss')</script></head><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p>Test</p></div></body>", cleanDocument.html());
        assertFalse(cleanDocument.head().text().contains("alert('xss')"));
    }

    @Test
    public void testIsValidDocument() {
        String html = "<html><head><title>Test</title></head><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        assertTrue(new Cleaner(DEFAULT_WHITELIST).isValid(dirtyDocument));

        html = "<html><head><script>alert('xss')</script></head><body><div><p>Test</p></div></body></html>";
        dirtyDocument = Jsoup.parse(html);
        assertFalse(new Cleaner(DEFAULT_WHITELIST).isValid(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithEmptyBody() {
        String html = "<html><body></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body></body>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithFrameset() {
        String html = "<html><head></head><frameset><frame src='frame1.html'></frameset></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<html><head></head></html>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithScriptsAndStyles() {
        String html = "<html><head><script src='script.js'></script><style>body { background-color: red; }</style></head><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p>Test</p></div></body>", cleanDocument.html());
        assertNull(cleanDocument.head().child(0));
        assertNull(cleanDocument.head().child(1));
    }

    @Test
    public void testCleanDocumentWithNestedElements() {
        String html = "<html><head></head><body><div><p><span>Nested test</span></p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p><span></span></p></div></body>", cleanDocument.html());
    }

    @Test
    public void testCleanDocumentWithAttributes() {
        String html = "<html><head></head><body><div class='test' id='testId' onclick='testFunction()'><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p>Test</p></div></body>", cleanDocument.html());
        assertNull(cleanDocument.head());
        Element div = cleanDocument.body().selectFirst("div");
        assertEquals(0, div.classNames().size());
        assertNull(div.id());
        assertNull(div.attr("onclick"));
    }

    @Test
    public void testCleanDocumentWithDataNodes() {
        String html = "<html><head></head><body><div><p><data>Test data</data></p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><p></p></div></body>", cleanDocument.html());
        assertNull(cleanDocument.head());
        Node dataNode = cleanDocument.body().selectFirst("p data");
        assertNull(dataNode);
    }

    @Test
    public void testCleanDocumentWithComplexElements() {
        String html = "<html><head></head><body><div class='test' id='testId' onclick='testFunction()'><a href='test.html'><img src='test.png' alt='Test image' />Test</a></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Cleaner(DEFAULT_WHITELIST).clean(dirtyDocument);
        assertEquals("<body><div><a href=''>Test</a></div></body>", cleanDocument.html());
        assertNull(cleanDocument.head());
        Element div = cleanDocument.body().selectFirst("div");
        assertEquals(0, div.classNames().size());
        assertNull(div.id());
        assertNull(div.attr("onclick"));
        Element a = div.selectFirst("a");
        assertEquals("", a.attr("href"));
        Element img = a.selectFirst("img");
        assertNull(img);
    }
}

class WhitelistTest {

    @Test
    public void testSimpleTextOnlyWhitelist() {
        Whitelist whitelist = Whitelist.simpleTextOnlyWhitelist();
        assertTrue(whitelist.isSafeTag("p"));
        assertTrue(whitelist.isSafeTag("div"));
        assertFalse(whitelist.isSafeTag("script"));
        assertFalse(whitelist.isSafeTag("style"));
        assertFalse(whitelist.isSafeTag("img"));
        assertFalse(whitelist.isSafeTag("a"));
        assertFalse(whitelist.isSafeAttribute("p", "onclick"));
        assertFalse(whitelist.isSafeAttribute("p", "src"));
        assertFalse(whitelist.isSafeAttribute("p", "href"));
        assertFalse(whitelist.isSafeAttribute("p", "class"));
        assertFalse(whitelist.isSafeAttribute("p", "id"));
        assertFalse(whitelist.isSafeAttribute("p", "style"));
    }

    @Test
    public void testCustomWhitelist() {
        Whitelist whitelist = Whitelist.simpleTagNameTextOnlyWhitelist("div", "p");
        assertTrue(whitelist.isSafeTag("div"));
        assertTrue(whitelist.isSafeTag("p"));
        assertFalse(whitelist.isSafeTag("script"));
        assertFalse(whitelist.isSafeTag("style"));
        assertFalse(whitelist.isSafeTag("img"));
        assertFalse(whitelist.isSafeTag("a"));
        assertFalse(whitelist.isSafeAttribute("div", "onclick"));
        assertFalse(whitelist.isSafeAttribute("div", "src"));
        assertFalse(whitelist.isSafeAttribute("div", "href"));
        assertFalse(whitelist.isSafeAttribute("div", "class"));
        assertFalse(whitelist.isSafeAttribute("div", "id"));
        assertFalse(whitelist.isSafeAttribute("div", "style"));
        assertTrue(whitelist.isSafeTag("p"));
        assertTrue(whitelist.isSafeAttribute("p", "class"));
        assertTrue(whitelist.isSafeAttribute("p", "style"));
    }

    @Test
    public void testCustomWhitelistWithEnforcedAttributes() {
        Whitelist whitelist = Whitelist.simpleTagNameTextOnlyWhitelist("div", "p");
        Whitelist customWhitelist = new Whitelist("div", "p")
                .addAttribute("div", "class")
                .addAttribute("div", "style")
                .addAttribute("p", "style");
        assertTrue(customWhitelist.isSafeTag("div"));
        assertTrue(customWhitelist.isSafeTag("p"));
        assertFalse(customWhitelist.isSafeTag("script"));
        assertFalse(customWhitelist.isSafeTag("style"));
        assertFalse(customWhitelist.isSafeTag("img"));
        assertFalse(customWhitelist.isSafeTag("a"));
        assertFalse(customWhitelist.isSafeAttribute("div", "onclick"));
        assertFalse(customWhitelist.isSafeAttribute("div", "src"));
        assertFalse(customWhitelist.isSafeAttribute("div", "href"));
        assertFalse(customWhitelist.isSafeAttribute("div", "class"));
        assertFalse(customWhitelist.isSafeAttribute("div", "id"));
        assertFalse(customWhitelist.isSafeAttribute("div", "style"));
        assertTrue(customWhitelist.isSafeTag("p"));
        assertTrue(customWhitelist.isSafeAttribute("p", "class"));
        assertTrue(customWhitelist.isSafeAttribute("p", "style"));
    }
}

class ElementMetaTest {

    @Test
    public void testElementMeta() {
        Element sourceEl = Element.parse("<p class='test' id='testId' onclick='testFunction()'>Test</p>");
        ElementMeta elementMeta = new Cleaner(DEFAULT_WHITELIST).createSafeElement(sourceEl);
        assertEquals("<p></p>", elementMeta.el.html());
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }
}