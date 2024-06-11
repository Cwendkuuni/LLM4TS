package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Tests for {@link Document} class.
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertFalse(doc.head() instanceof Document);
        assertFalse(doc.body() instanceof Document);
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        Element html = doc.getElementsByTag("html").first();
        assertSame(doc, html.parent());
        Element head = html.getElementsByTag("head").first();
        assertSame(html, head.parent());
        Element body = html.getElementsByTag("body").first();
        assertSame(html, body.parent());
        assertNull(doc.title());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);
        doc.title("Test Title");
        head = doc.head();
        assertSame(doc, head.parent());
        assertEquals("Test Title", head.text());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        doc.title("Test Title");
        body = doc.body();
        assertSame(doc, body.parent());
        assertNull(body.text());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("baseUri", element.baseUri());
        assertFalse(element.parent() instanceof Document);
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        Element title = doc.getElementsByTag("title").first();
        Element body = doc.createElement("body");
        Element p = doc.createElement("p");
        p.text("Test text");
        body.appendChild(p);
        doc.appendChild(title);
        doc.appendChild(body);

        doc.normalise();

        Element html = doc.findFirstElementByTagName("html", doc);
        assertSame(doc, html.parent());
        Element head = html.getElementsByTag("head").first();
        assertSame(html, head.parent());
        Element titleEl = head.getElementsByTag("title").first();
        assertSame(head, titleEl.parent());
        assertEquals("Test Title", titleEl.text());
        Element bodyEl = html.getElementsByTag("body").first();
        assertSame(html, bodyEl.parent());
        Element textNode = bodyEl.getElementsByTag("p").first().childNodes().get(0);
        assertEquals("Test text", textNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeWithNullNode() {
        Document doc = new Document("baseUri");
        doc.normalise(null);
    }

    @Test
    public void testNormalizeWithoutElements() {
        Document doc = new Document("baseUri");
        doc.normalise();
        Element html = doc.findFirstElementByTagName("html", doc);
        assertSame(doc, html.parent());
        assertNull(html.getElementsByTag("head").first());
        assertNull(html.getElementsByTag("body").first());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.findFirstElementByTagName("html", doc);
        assertSame(doc, html.parent());
        Element head = doc.findFirstElementByTagName("head", doc);
        assertSame(html, head.parent());
        Element body = doc.findFirstElementByTagName("body", doc);
        assertSame(html, body.parent());
        Element title = doc.findFirstElementByTagName("title", doc);
        assertSame(head, title.parent());
        assertNull(doc.findFirstElementByTagName("nonExistentTag", doc));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        Element title = doc.getElementsByTag("title").first();
        title.text("Test Title 2");

        assertEquals("<html><head><title>Test Title 2</title></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        Element body = doc.body();
        TextNode textNode = doc.createTextNode("Test text");
        body.appendChild(textNode);

        assertEquals("Test text", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneFailed() {
        Document doc = new Document("baseUri");
        doc.clone();
    }

    @Test
    public void testClone() {
        Document doc = new Document("baseUri");
        Document clonedDoc = doc.clone();
        assertEquals(doc.outputSettings(), clonedDoc.outputSettings());
    }
}