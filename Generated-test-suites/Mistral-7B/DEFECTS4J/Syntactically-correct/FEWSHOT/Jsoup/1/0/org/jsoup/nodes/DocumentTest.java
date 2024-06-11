package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().name().equals("head"));
        assertTrue(doc.body().name().equals("body"));
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc = Document.createShell("http://example.com");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc = Document.createShell("http://example.com");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());

        doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element p1 = doc.createElement("p").text("Text 1");
        Element p2 = doc.createElement("p").text("Text 2");
        Element div = doc.createElement("div");
        Element title = doc.createElement("title").text("Test Title");

        doc.appendElement("html").appendElement(title);
        doc.appendElement("html").appendElement("head").appendElement(title);
        doc.appendElement("html").appendElement("body").appendChild(p1);
        doc.appendElement("html").appendElement("body").appendElement(div);
        doc.appendElement("html").appendElement("body").appendChild(p2);

        doc.normalise();

        List<Element> elements = doc.select("body > *");
        assertEquals(3, elements.size());
        assertSame(p1, elements.get(0));
        assertSame(p2, elements.get(1));
        assertSame(div, elements.get(2));

        List<TextNode> textNodes = doc.select("text()");
        assertEquals(2, textNodes.size());
        assertEquals("Text 1", textNodes.get(0).text());
        assertEquals("Text 2", textNodes.get(1).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p").text("Test");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("html").appendElement("body").appendChild(p);

        assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test");
        assertEquals("Test", doc.body().text());
    }
}