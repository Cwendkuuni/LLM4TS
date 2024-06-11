package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#root", doc.tagName());
        assertEquals(3, doc.childNodes().size());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#root", doc.tagName());
        assertEquals(5, doc.childNodes().size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        assertEquals("#head", head.tagName());
        assertEquals(doc, head.parent());
        assertEquals(0, head.childNodes().size());
    }

    @Test
    public void testGetBodyElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();
        assertEquals("#body", body.tagName());
        assertEquals(doc, body.parent());
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
        assertEquals("#document", doc.tagName());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
        assertEquals("#document", doc.tagName());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        assertEquals("#p", p.tagName());
        assertEquals(baseUri, p.baseUri());
        assertNull(p.parent());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.head().appendText("Hello");
        doc.text("World");
        doc.normalise();
        Element body = doc.body();
        assertEquals("HelloWorld", body.text().trim());
        assertEquals("#document", doc.tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title").text("Title");
        html.appendChild(head);
        head.appendChild(title);
        head.appendChild(doc.createElement("script").src("script.js"));
        html.appendChild(body);
        Element script = doc.findFirstElementByTagName("script", html);
        assertEquals("#script", script.tagName());
        assertEquals(html, script.parent());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title").text("Title");
        Element body = doc.appendElement("body");
        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(title);
        body.appendText("Hello World");
        String htmlString = doc.outerHtml();
        assertEquals("<html><head><title>Title</title></head><body>Hello World</body></html>", htmlString);
    }

    @Test
    public void testText() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();
        body.text("Hello World");
        String text = doc.text();
        assertEquals("Hello World", text);
    }
}