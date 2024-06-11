package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        Element head = document.head();
        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testBodyElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        body.appendText("Test Body Text");
        assertEquals("Test Body Text", body.text());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleSetter() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title(null);
        assertNull(document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.head().appendText("Test Head Text");
        document.body().appendText("Test Body Text 1");
        document.appendText("Test Body Text 2");
        document.normalise();
        List<Node> bodyChildren = document.body().children();
        assertEquals(2, bodyChildren.size());
        TextNode headText = (TextNode) document.head().child(0);
        assertEquals("Test Head Text", headText.text());
        TextNode bodyText1 = (TextNode) bodyChildren.get(0);
        assertEquals("Test Body Text 1", bodyText1.text());
        TextNode bodyText2 = (TextNode) bodyChildren.get(1);
        assertEquals("Test Body Text 2", bodyText2.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Body Text");
        Element title = document.findFirstElementByTagName("title", document);
        assertEquals("title", title.tagName());
        assertEquals("Test Title", title.text());
        Element p = document.findFirstElementByTagName("p", document);
        assertEquals("p", p.tagName());
        assertEquals("Test Body Text", p.text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Body Text");
        String expected = "<html><head><title>Test Title</title></head><body><p>Test Body Text</p></body></html>";
        assertEquals(expected, document.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.body().text("Test Body Text");
        assertEquals("Test Body Text", document.text());
    }
}