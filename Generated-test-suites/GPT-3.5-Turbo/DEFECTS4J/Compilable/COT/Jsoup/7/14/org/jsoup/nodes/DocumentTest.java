package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("html", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);

        document.head().appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element div = document.createElement("div");
        div.text("Hello");
        document.appendChild(div);
        document.normalise();
        Element body = document.body();
        assertNotNull(body);
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello", body.childNodes().get(0).toString());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element div = document.createElement("div");
        div.text("Hello");
        document.appendChild(div);
        String outerHtml = document.outerHtml();
        assertEquals("<div>Hello</div>", outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.body().text("Hello");
        String bodyText = document.body().text();
        assertEquals("Hello", bodyText);
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }
}

