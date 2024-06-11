package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class DocumentTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateShell() {
        assertEquals("html", doc.tagName());
        assertSame(doc, doc.head());
        assertSame(doc, doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testDocument() {
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title");

        assertSame(head, doc.findFirstElementByTagName("head", doc));
        assertSame(body, doc.findFirstElementByTagName("body", doc));
        assertSame(title, doc.findFirstElementByTagName("title", doc));
        assertNull(doc.findFirstElementByTagName("nonexistent", doc));
    }

    @Test
    public void testGetElementsByTag() {
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title1 = doc.appendElement("title");
        Element title2 = doc.appendElement("title");

        Elements elements = doc.getElementsByTag("title");
        assertEquals(2, elements.size());
        assertSame(title1, elements.get(0));
        assertSame(title2, elements.get(1));
        assertSame(elements, doc.getElementsByTag("title", doc));
    }

    @Test
    public void testNormalise() {
        doc.appendElement("title", "Test Title");
        doc.text("Test Text");
        doc.appendElement("div", "Test Div");

        doc.normalise();

        assertEquals("html", doc.tagName());
        assertSame(doc, doc.head());
        assertSame(doc, doc.body());
        assertSame(doc.title(), doc.getElementsByTag("title").first());
        assertNull(doc.titleElementSibling());
        assertSame(doc.body(), doc.getElementsByTag("body").first());
        assertSame(doc.text(), doc.body().getElementsByTag("textNode").first().text());
        assertNull(doc.body().getElementsByTag("div").first());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        TextNode textNode1 = doc.appendTextNode("Test Text 1");
        TextNode textNode2 = doc.appendTextNode("Test Text 2");
        TextNode textNode3 = doc.appendTextNode("Test Text 3");
        TextNode textNode4 = doc.appendTextNode("Test Text 4");

        doc.normaliseTextNodes(head);
        doc.normaliseTextNodes(body);
        doc.normaliseTextNodes(doc);

        assertEquals(4, head.childNodeSize());
        assertEquals(4, body.childNodeSize());
        assertEquals(4, doc.childNodeSize());

        assertSame(textNode1, doc.getElementsByTag("textNode").first());
        assertSame(textNode2, doc.getElementsByTag("textNode").get(1));
        assertSame(textNode3, doc.getElementsByTag("textNode").get(2));
        assertSame(textNode4, doc.getElementsByTag("textNode").get(3));
    }

    @Test
    public void testNormaliseStructure() {
        Element head1 = doc.appendElement("head");
        Element head2 = doc.appendElement("head");
        Element body1 = doc.appendElement("body");
        Element body2 = doc.appendElement("body");

        doc.normaliseStructure("head", doc);
        assertEquals(1, doc.getElementsByTag("head").size());
        assertSame(head1, doc.getElementsByTag("head").first());
        assertNull(head2);

        doc.normaliseStructure("body", doc);
        assertEquals(1, doc.getElementsByTag("body").size());
        assertSame(body1, doc.getElementsByTag("body").first());
        assertNull(body2);
    }

    @Test
    public void testOuterHtml() {
        doc.appendElement("title", "Test Title");
        doc.appendElement("body").appendElement("h1", "Test Heading");

        String expected = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        doc.appendElement("title", "Test Title");
        doc.appendElement("body").text("Test Text");

        String expected = "Test Text";
        assertEquals(expected, doc.text());
    }

    @Test
    public void testClone() {
        doc.appendElement("title", "Test Title");
        doc.appendElement("body").appendElement("h1", "Test Heading");

        Document clone = doc.clone();

        String expected = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        assertEquals(expected, clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document docWithSettings = Document.createShell("http://example.com").outputSettings(new OutputSettings().prettyPrint(false));

        assertFalse(docWithSettings.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document docWithSettings = Document.createShell("http://example.com").outputSettings().charset(StandardCharsets.ISO_8859_1);

        assertEquals(StandardCharsets.ISO_8859_1, docWithSettings.outputSettings().charset());
    }

    @Test
    public void testOutputSettingsEncode() {
        Document docWithSettings = Document.createShell("http://example.com").outputSettings().charset("ISO-8859-1");

        assertEquals(StandardCharsets.ISO_8859_1, docWithSettings.outputSettings().charset());
        assertEquals("ISO-8859-1", docWithSettings.outputSettings().charset().name());
        assertEquals("ISO-8859-1", docWithSettings.outputSettings().encoder().name());
    }
}