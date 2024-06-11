package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testDocument() {
        Document doc = new Document("http://www.example.com");

        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");

        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");

        Element head = doc.head();
        assertNull(head);

        Element html = doc.appendElement("html");
        html.appendElement("head");

        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");

        Element body = doc.body();
        assertNull(body);

        Element html = doc.appendElement("html");
        html.appendElement("body");

        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");

        String title = doc.title();
        assertEquals("", title);

        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        head.appendElement("title").text("Example Title");

        title = doc.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://www.example.com");

        doc.title("Example Title");

        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element title = head.getElementsByTag("title").first();

        assertNotNull(title);
        assertEquals("Example Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");

        Element element = doc.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");

        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);

        List<Node> nodes = body.childNodes;
        for (Node node : nodes) {
            assertTrue(node instanceof TextNode);
        }
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");

        Element html = doc.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");

        String outerHtml = doc.outerHtml();
        assertEquals("<html><head></head><body></body></html>", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");

        doc.text("Example Text");

        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("Example Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");

        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }
}