package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;

public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any setup here
    }

    // Test constructor
    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
    }

    // Test createShell method
    @org.junit.Test
    public void testCreateShell() {
        String baseUri = "baseUri";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    // Test head method
    @org.junit.Test
    public void testHeadMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element head = document.head();
        assertNotNull(head);
    }

    // Test body method
    @org.junit.Test
    public void testBodyMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element body = document.body();
        assertNotNull(body);
    }

    // Test title method (get)
    @org.junit.Test
    public void testTitleMethodGet() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    // Test title method (set)
    @org.junit.Test
    public void testTitleMethodSet() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.title("Test Title");
        document.title("New Title");
        String newTitle = document.title();
        assertEquals("New Title", newTitle);
    }

    // Test createElement method
    @org.junit.Test
    public void testCreateElementMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
    }

    // Test normalise method
    @org.junit.Test
    public void testNormaliseMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Title");
        document.appendElement("body").appendText("Text");
        document.normalise();
        Element body = document.body();
        assertEquals("Title\nText", body.html());
    }

    // Test findFirstElementByTagName method
    @org.junit.Test
    public void testFindFirstElementByTagNameMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Title");
        document.appendElement("html").appendElement("body").appendElement("p").text("Paragraph");
        Element title = document.findFirstElementByTagName("title", document);
        assertNotNull(title);
        assertEquals("Title", title.text());

        Element p = document.findFirstElementByTagName("p", document);
        assertNotNull(p);
        assertEquals("Paragraph", p.text());
    }

    // Test outerHtml method (get)
    @org.junit.Test
    public void testOuterHtmlMethodGet() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html").appendElement("head").appendElement("title").text("Title");
        Element body = document.appendElement("body").appendElement("p").text("Paragraph");
        String htmlString = document.outerHtml();
        assertEquals("<html><head><title>Title</title></head><body><p>Paragraph</p></body></html>", htmlString);
    }

    // Test text method (get)
    @org.junit.Test
    public void testTextMethodGet() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element body = document.appendElement("body").appendElement("p").text("Paragraph");
        String text = document.text();
        assertEquals("Paragraph", text);
    }

    // Test text method (set)
    @org.junit.Test
    public void testTextMethodSet() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.text("Test Text");
        Element body = document.body();
        body.appendElement("p").text("Paragraph");
        document.text("New Test Text");
        String newText = document.text();
        assertEquals("New Test Text", newText);
        assertEquals("New Test Text\n<p>Paragraph</p>", document.outerHtml());
    }

    // Test nodeName method
    @org.junit.Test
    public void testNodeNameMethod() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
    }

    // Test title method (get) with empty title
    @org.junit.Test
    public void testTitleMethodGetEmpty() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        String title = document.title();
        assertEquals("", title);
    }

    // Test title method (set) with null title
    @org.junit.Test(expected = NullPointerException.class)
    public void testTitleMethodSetNull() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.title(null);
    }

    // Test title method (set) with invalid tag name
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTitleMethodSetInvalidTagName() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.title("InvalidTagName");
    }

    // Test createElement method with null tagName
    @org.junit.Test(expected = NullPointerException.class)
    public void testCreateElementMethodNullTagName() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        document.createElement(null);
    }

    // Test normaliseTextNodes method with empty Element
    @org.junit.Test
    public void testNormaliseTextNodesEmptyElement() {
        Document document = new Document();
        document.normaliseTextNodes(document);
    }

    // Test normaliseTextNodes method with non-empty Element and one TextNode to move
    @org.junit.Test
    public void testNormaliseTextNodesOneTextNode() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element element = document.appendElement("element");
        TextNode textNode = document.createElement("").appendText("TextNode");
        element.appendChild(textNode);

        document.normaliseTextNodes(element);

        Element body = document.body();
        TextNode bodyTextNode = body.getTextNodes().get(0);
        assertEquals("TextNode", bodyTextNode.text());
        assertEquals(1, body.childNodes().size());
    }

    // Test normaliseTextNodes method with non-empty Element and multiple TextNodes to move
    @org.junit.Test
    public void testNormaliseTextNodesMultipleTextNodes() {
        String baseUri = "baseUri";
        Document document = new Document(baseUri);
        Element element = document.appendElement("element");
        TextNode textNode1 = document.createElement("").appendText("TextNode1");
        TextNode textNode2 = document.createElement("").appendText("TextNode2");
        TextNode textNode3 = document.createElement("").appendText("TextNode3");
        element.appendChild(textNode1);
        element.appendChild(textNode2);
        element.appendChild(textNode3);

        document.normaliseTextNodes(element);

        Element body = document.body();
        TextNode bodyTextNode1 = body.getTextNodes().get(0);
        TextNode bodyTextNode2 = body.getTextNodes().get(1);
        TextNode bodyTextNode3 = body.getTextNodes().get(2);
        assertEquals("TextNode1TextNode2TextNode3", body.html());
        assertEquals("TextNode1", bodyTextNode1.text());
        assertEquals("TextNode2", bodyTextNode2.text());
        assertEquals("TextNode3", bodyTextNode3.text());
    }
}