package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        html = doc.body();
        assertNotNull(html);
        assertEquals("body", html.nodeName());
    }

    @Test
    public void testGetHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc = Document.createShell("http://example.com");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc = Document.createShell("http://example.com");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.appendElement("title").text("Title");
        Element body = doc.appendElement("body").text("Body");
        Element div = doc.createElement("div").text("Div");
        doc.appendChild(title);
        doc.appendChild(body);
        doc.appendChild(div);

        doc.normalise();

        List<Node> titleNodes = doc.getElementsByTag("title");
        List<Node> bodyNodes = doc.getElementsByTag("body");
        List<Node> divNodes = doc.getElementsByTag("div");

        assertEquals(1, titleNodes.size());
        assertEquals("Title", titleNodes.get(0).text());

        assertEquals(1, bodyNodes.size());
        assertEquals("Body", bodyNodes.get(0).text());

        assertEquals(1, divNodes.size());
        assertEquals("Div", divNodes.get(0).nodeName());
        assertEquals("", divNodes.get(0).text());

        Element newTitle = doc.createElement("title").text("New Title");
        doc.head().appendChild(newTitle);
        doc.normalise();

        titleNodes = doc.getElementsByTag("title");
        assertEquals(1, titleNodes.size());
        assertEquals("New Title", titleNodes.get(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", this);
        assertEquals(null, title);

        title = doc.findFirstElementByTagName("title", html);
        assertEquals("title", title.nodeName());

        title = doc.findFirstElementByTagName("title", head);
        assertEquals(null, title);

        title = doc.findFirstElementByTagName("title", body);
        assertEquals(null, title);

        title = doc.findFirstElementByTagName("title", doc);
        assertEquals("title", title.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title").text("Title");

        String htmlString = doc.outerHtml();
        assertEquals("<html><head><title>Title</title></head><body></body></html>", htmlString);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.appendElement("body").text("Body");

        String text = doc.text();
        assertEquals("Body", text);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}