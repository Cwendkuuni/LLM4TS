package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any mocks or setup objects as needed
    }

    @org.junit.Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertInstanceOf(Document.class, document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.head();
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @org.junit.Test
    public void testTitleNull() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        // Add elements and text nodes for testing
        Element html = document.head();
        html.appendChild(new TextNode("Test Text 1"));
        html.appendChild(new Element("h1").text("Test Heading 1"));

        Element head = document.createElement("head");
        html.appendChild(head);
        head.appendChild(new TextNode("Test Text 2"));
        head.appendChild(new Element("title").text("Test Title 2"));

        Element body = document.createElement("body");
        html.appendChild(body);
        body.appendChild(new TextNode("Test Text 3"));
        body.appendChild(new Element("p").text("Test Paragraph 1"));

        document.normalise();

        Element title = document.title();
        assertEquals("Test Title 2", title.text());
        Element bodyElement = document.body();
        TextNode testText1 = (TextNode) bodyElement.getElementsByTag("h1").first().child(0);
        TextNode testText3 = (TextNode) bodyElement.getElementsByTag("p").first().child(0);
        assertEquals("Test Text 1", testText1.text());
        assertEquals("Test Text 3", testText3.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("baseUri");
        Node node = null;
        Element element = document.findFirstElementByTagName("tag", node);
        assertNull(element);
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameEmptyDocument() {
        Document document = new Document("baseUri");
        Element element = document.findFirstElementByTagName("tag", document);
        assertNull(element);
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameElementMatch() {
        Document document = new Document("baseUri");
        Element html = document.head();
        html.appendChild(new Element("html"));
        Element head = html.appendElement("head");
        head.appendChild(new Element("title").text("Test Title"));
        Element body = html.appendElement("body");
        body.appendChild(new Element("p").text("Test Paragraph"));

        Element title = document.findFirstElementByTagName("title", document);
        assertNotNull(title);
        assertEquals("title", title.tagName());
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameNoMatch() {
        Document document = new Document("baseUri");
        Element html = document.head();
        html.appendChild(new Element("html"));
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element element = document.findFirstElementByTagName("div", document);
        assertNull(element);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.head();
        html.appendChild(new Element("html"));
        Element head = html.appendElement("head");
        head.appendChild(new Element("title").text("Test Title"));
        Element body = html.appendElement("body");
        body.appendChild(new Element("p").text("Test Paragraph"));

        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Text");

        assertEquals("Test Text", document.text());
    }

    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Document document = new Document("baseUri");
        document.clone();
    }
}