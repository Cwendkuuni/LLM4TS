package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testNewEmptyDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element div = doc.createElement("div");
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element h1 = doc.createElement("h1").text("Test");
        Element p = doc.createElement("p").text("This is a test.");
        doc.body().appendChild(h1);
        doc.body().appendChild(p);

        doc.normalise();

        Element body = doc.body();
        List<Node> children = body.childNodes();
        assertEquals(2, children.size());
        TextNode textNode = (TextNode) children.get(0);
        assertEquals("", textNode.text());
        assertEquals("Test", children.get(1).text());
        assertEquals("This is a test.", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");

        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        assertEquals(head, doc.findFirstElementByTagName("head", this));
        assertEquals(body, doc.findFirstElementByTagName("body", this));
        assertNull(doc.findFirstElementByTagName("footer", this));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element h1 = doc.createElement("h1").text("Test");
        Element p = doc.createElement("p").text("This is a test.");
        doc.body().appendChild(h1);
        doc.body().appendChild(p);

        doc.normalise();

        String outerHtml = doc.outerHtml();
        String expected = "<html><head></head><body><h1>Test</h1><p>This is a test.</p></body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.createElement("body").text("Test");
        doc.appendChild(body);

        String text = doc.text();
        assertEquals("Test", text);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Element h1 = doc.createElement("h1").text("Test");
        doc.body().appendChild(h1);

        Document clonedDoc = doc.clone();
        assertEquals(h1.text(), clonedDoc.head().text());
    }
}