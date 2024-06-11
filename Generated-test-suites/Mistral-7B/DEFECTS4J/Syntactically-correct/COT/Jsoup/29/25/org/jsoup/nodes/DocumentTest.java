package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testCreateDocument() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.tagName());
    }

    @org.junit.Test
    public void testGetHeadElement() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testGetBodyElement() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testGetStringTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test
    public void testSetStringTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);

        document.title("New Test Title");
        title = document.title();
        assertEquals("New Test Title", title);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        TextNode textNode1 = document.appendTextNode("Text 1");
        TextNode textNode2 = document.appendTextNode("Text 2");
        TextNode textNode3 = document.appendTextNode("Text 3");

        document.normalise();

        List<Node> children = body.childNodes();
        assertEquals(3, children.size());
        Node node = children.get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Text 1", ((TextNode) node).text());
        node = children.get(1);
        assertTrue(node instanceof TextNode);
        assertEquals("Text 2", ((TextNode) node).text());
        node = children.get(2);
        assertTrue(node instanceof TextNode);
        assertEquals("Text 3", ((TextNode) node).text());

        children = html.childNodes();
        assertEquals(3, children.size());
        node = children.get(0);
        assertTrue(node instanceof Element);
        assertEquals("head", node.tagName());
        node = children.get(1);
        assertTrue(node instanceof Element);
        assertEquals("body", node.tagName());
        node = children.get(2);
        assertTrue(node instanceof Element);
        assertEquals("html", node.tagName());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title");
        Element body = document.appendElement("body");

        Element element = document.findFirstElementByTagName("title", html);
        assertEquals(title, element);

        element = document.findFirstElementByTagName("html", body);
        assertEquals(html, element);

        element = document.findFirstElementByTagName("title", body);
        assertNull(element);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title");
        title.text("Test Title");
        Element body = document.appendElement("body");
        body.text("Test Body");

        String htmlString = document.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body>Test Body</body></html>";
        assertEquals(expected, htmlString);
    }

    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Body");

        String text = document.text();
        assertEquals("Test Body", text);

        TextNode textNode = document.appendTextNode("Appended Text");
        text = document.text();
        assertEquals("Test BodyAppended Text", text);
    }

    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testClone() {
        Document document = new Document("baseUri");
        Document clonedDocument = document.clone();

        OutputSettings outputSettings = document.outputSettings();
        assertEquals(outputSettings, clonedDocument.outputSettings());
    }
}