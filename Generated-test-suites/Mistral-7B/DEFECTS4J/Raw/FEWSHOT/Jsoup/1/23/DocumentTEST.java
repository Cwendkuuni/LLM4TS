package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Document.createShell;
import static org.jsoup.nodes.Elements.html;
import static org.jsoup.nodes.Elements.title;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testGetHead() {
        Document doc = createShell("http://example.com");
        Element head = doc.head();
        assertTrue(head.tagName().equals("head"));
        assertNull(head.text());
    }

    @Test
    public void testGetBody() {
        Document doc = createShell("http://example.com");
        Element body = doc.body();
        assertTrue(body.tagName().equals("body"));
        assertNull(body.text());
    }

    @Test
    public void testGetTitle() {
        Document doc = createShell("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = createShell("http://example.com");
        doc.title("Test Title");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = createShell("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = createShell("http://example.com");
        Element p = doc.createElement("p").text("Test");
        Element div = doc.createElement("div").text("Div");
        doc.body().appendChild(p);
        doc.body().appendChild(div);
        doc.normalise();
        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(2, bodyChildren.size());
        assertEquals("Test", bodyChildren.get(0).text());
        assertEquals("Div", bodyChildren.get(1).tagName());
    }

    @Test
    public void testNormaliseDocumentWithEmptyElements() {
        Document doc = createShell("http://example.com");
        Element div = doc.createElement("div");
        doc.head().appendChild(div);
        doc.normalise();
        List<Node> headChildren = doc.head().childNodes();
        assertEquals(0, headChildren.size());
    }

    @Test
    public void testNormaliseDocumentWithEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        List<Node> children = doc.childNodes();
        assertEquals(1, children.size());
        assertEquals("#document", children.get(0).nodeName());
    }

    @Test
    public void testNormaliseDocumentWithEmptyBody() {
        Document doc = createShell("http://example.com");
        doc.body().text("");
        doc.normalise();
        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(0, bodyChildren.size());
    }

    @Test
    public void testNormaliseDocumentWithTextNodesInRoot() {
        Document doc = new Document("http://example.com");
        doc.text("Test");
        doc.normalise();
        List<Node> children = doc.childNodes();
        assertEquals(1, children.size());
        assertEquals("Test", children.get(0).text());
    }

    @Test
    public void testNormaliseDocumentWithTextNodesInHtml() {
        Document doc = createShell("http://example.com");
        Element html = doc.appendElement("html").text("Test");
        doc.normalise();
        List<Node> children = doc.childNodes();
        assertEquals(1, children.size());
        assertEquals("#html", children.get(0).nodeName());
        List<Node> htmlChildren = children.get(0).childNodes();
        assertEquals(1, htmlChildren.size());
        assertEquals("Test", htmlChildren.get(0).text());
    }

    @Test
    public void testNormaliseDocumentWithTextNodesInHead() {
        Document doc = createShell("http://example.com");
        Element head = doc.head();
        head.appendText("Test");
        doc.normalise();
        List<Node> headChildren = doc.head().childNodes();
        assertEquals(1, headChildren.size());
        assertEquals("Test", headChildren.get(0).text());
    }

    @Test
    public void testNormaliseDocumentWithTextNodesInBody() {
        Document doc = createShell("http://example.com");
        Element body = doc.body();
        body.appendText("Test");
        doc.normalise();
        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(1, bodyChildren.size());
        assertEquals("Test", bodyChildren.get(0).text());
    }

    @Test
    public void testNormaliseDocumentWithTextNodesInRootAndBody() {
        Document doc = createShell("http://example.com");
        doc.text("Test in root");
        Element p = doc.createElement("p").text("Test in body");
        doc.body().appendChild(p);
        doc.normalise();
        List<Node> children = doc.childNodes();
        assertEquals(1, children.size());
        assertEquals("#document", children.get(0).nodeName());
        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(1, bodyChildren.size());
        assertEquals("Test in body", bodyChildren.get(0).text());
    }
}