package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testEmptyDocumentCreation() {
        Document doc = new Document("base-uri");
        assertEquals("#root", doc.tagName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("base-uri");
        Element html = doc.head();
        assertEquals("html", html.tagName());
        html = doc.head().getFirstElementByTagName("head");
        assertEquals("head", html.tagName());
        html = doc.head().getFirstElementByTagName("body");
        assertEquals("body", html.tagName());
    }

    @Test
    public void testEmptyTitle() {
        Document doc = new Document("base-uri");
        assertNull(doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("base-uri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("base-uri");
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("base-uri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode text1 = body.appendText("Text node 1");
        TextNode text2 = body.appendText("Text node 2");
        TextNode text3 = body.appendText("Text node 3");

        doc.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(1, bodyChildren.size());
        Node bodyChild = bodyChildren.get(0);
        assertEquals(TextNode.class, bodyChild.getClass());
        assertEquals("Text node 1Text node 2Text node 3", bodyChild.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        Document doc = new Document("base-uri");
        doc.createElement("x");
    }

    @Test
    public void testTitleWithExistingElement() {
        Document doc = new Document("base-uri");
        Element title = doc.head().getFirstElementByTagName("title");
        doc.title("Test Title");
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testTitleWithNewElement() {
        Document doc = new Document("base-uri");
        doc.title("Test Title");
        Element title = doc.head().getFirstElementByTagName("title");
        assertEquals("Test Title", title.text().trim());
    }
}