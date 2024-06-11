package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertEquals("#html", html.nodeName());
        Element head = html.head();
        assertEquals("#head", head.nodeName());
        Element body = html.body();
        assertEquals("#body", body.nodeName());
        assertNull(doc.title());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);
        doc.head = new Element("head");
        head = doc.head();
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        doc.body = new Element("body");
        body = doc.body();
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());
        doc.title = new Element("title").text("Title");
        assertEquals("Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Title");
        assertEquals("Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("tagName");
        assertEquals("#tagName", element.nodeName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Title").appendElement("body")
                .appendText("First Text").appendText(" Second Text");
        doc.normalise();
        Element html = doc.head();
        Element head = html.head();
        Element title = head.getElementsByTag("title").first();
        assertEquals("#head", html.nodeName());
        assertEquals("#html", head.nodeName());
        assertEquals("#title", title.nodeName());
        assertEquals("Title", title.text());
        Element body = html.body();
        assertEquals("#body", body.nodeName());
        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                textNodes.add(textNode);
            }
        }
        assertEquals(2, textNodes.size());
        TextNode firstTextNode = textNodes.get(0);
        assertEquals(" First Text", firstTextNode.text());
        TextNode secondTextNode = textNodes.get(1);
        assertEquals("  Second Text", secondTextNode.text());
    }
}