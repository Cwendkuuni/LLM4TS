package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("html", doc.head().nodeName());
        assertEquals("head", doc.head().firstElementChild().nodeName());
        assertEquals("head", doc.head().lastElementChild().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title").text("Test Title");
        Element body = doc.appendElement("body");

        doc.appendChild(html);
        html.appendChild(head);
        head.appendChild(title);
        body.appendChild(doc.createElementTextNode("Test"));
        body.appendChild(title);

        doc.normalise();

        assertSame(title, doc.head().firstElementChild());
        assertSame(title, body().firstChild());
        assertEquals("Test", body().text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title").text("Test Title");
        Element p = doc.createElement("p").text("Test Paragraph");

        doc.head().appendChild(title);
        doc.body().appendChild(p);

        Element foundTitle = doc.findFirstElementByTagName("title", this);
        assertSame(title, foundTitle);

        Element foundP = doc.findFirstElementByTagName("p", this);
        assertSame(p, foundP);

        Element notFound = doc.findFirstElementByTagName("div", this);
        assertNull(notFound);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title").text("Test Title");
        Element p = doc.createElement("p").text("Test Paragraph");

        doc.head().appendChild(title);
        doc.body().appendChild(p);

        String outerHtml = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title").text("Test Title");
        Element p = doc.createElement("p").text("Test Paragraph");

        doc.head().appendChild(title);
        doc.body().appendChild(p);

        doc.body().text("Test Text");

        String text = doc.body().text();
        assertEquals("Test TextTest Paragraph", text);
    }
}