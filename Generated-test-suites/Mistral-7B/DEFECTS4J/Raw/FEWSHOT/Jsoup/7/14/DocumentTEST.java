package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNull(head);
        document = Document.createShell("http://example.com");
        head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testGetBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNull(body);
        document = Document.createShell("http://example.com");
        body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
        document = Document.createShell("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.appendText("Test text in body");
        document.head().appendText("Test text in head");
        document.head().appendElement(document.createElement("title")).text("Test Title");
        document.normalise();
        assertEquals("Test Title", document.title());
        Element body = document.body();
        assertEquals("Test text in body", body.text());
        List<Node> headNodes = document.head().childNodes();
        assertEquals(1, headNodes.size());
        assertEquals("title", headNodes.get(0).nodeName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = Document.createShell("http://example.com");
        Element html = document.findFirstElementByTagName("html", document);
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        Element head = document.findFirstElementByTagName("head", document);
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        Element body = document.findFirstElementByTagName("body", document);
        assertNotNull(body);
        assertEquals("body", body.nodeName());

        Element title = document.findFirstElementByTagName("title", document);
        assertNull(title);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test text");
        assertEquals("Test text", document.text());

        Document documentWithBody = Document.createShell("http://example.com");
        documentWithBody.body().text("Test text in body");
        assertEquals("Test text in body", documentWithBody.text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = Document.createShell("http://example.com");
        document.head().appendText("Test text 1");
        document.head().appendText("Test text 2");
        document.head().appendText("Test text 3");
        document.normalise();
        Element body = document.body();
        List<Node> bodyChildNodes = body.childNodes();
        assertEquals(3, bodyChildNodes.size());
        TextNode textNode1 = (TextNode) bodyChildNodes.get(0);
        assertEquals("Test text 1", textNode1.text());
        TextNode textNode2 = (TextNode) bodyChildNodes.get(1);
        assertEquals("Test text 2", textNode2.text());
        TextNode textNode3 = (TextNode) bodyChildNodes.get(2);
        assertEquals("Test text 3", textNode3.text());
    }
}