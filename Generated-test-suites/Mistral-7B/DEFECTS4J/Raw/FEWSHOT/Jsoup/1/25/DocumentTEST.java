package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#root", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#root", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("#a", element.nodeName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.head();
        Element title = doc.createElement("title").text("Test Title");
        Element body = doc.createElement("body");
        Element textNode = doc.createElement("TextNode").text("Test Text");

        html.appendChild(head);
        head.appendChild(title);
        head.appendChild(textNode);
        doc.appendChild(html);

        doc.normalise();

        List<Node> textNodes = new ArrayList<>();
        for (Node node: doc.childNodes()) {
            if (node instanceof TextNode && !node.isBlank()) {
                textNodes.add(node);
            }
        }

        assertEquals(1, textNodes.size());
        assertSame(textNode, textNodes.get(0));
        assertEquals("Test Text", textNodes.get(0).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.head();
        Element title = doc.createElement("title").text("Test Title");
        Element body = doc.createElement("body").text("Test Body");

        html.appendChild(head);
        head.appendChild(title);
        doc.appendChild(html);

        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.body().text("Test Body");
        assertEquals("Test Body", doc.text().trim());
    }
}