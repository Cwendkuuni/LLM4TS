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
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetDocumentTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetDocumentTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertNull(doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Hello World");
        doc.head().appendText("Test Heading");

        doc.normalise();

        Element bodyElement = doc.body();
        assertNotNull(bodyElement);
        assertEquals("Hello World", bodyElement.text());
        assertEquals(0, bodyElement.childElementsSize());

        Element headElement = doc.head();
        assertNotNull(headElement);
        assertEquals("Test Heading", headElement.text());
        assertEquals(0, headElement.childElementsSize());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element htmlElement = doc.createElement("html");
        Element headElement = doc.createElement("head");
        Element bodyElement = doc.createElement("body");

        doc.appendChild(htmlElement);
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);

        Element element = doc.findFirstElementByTagName("body", htmlElement);
        assertEquals(bodyElement, element);

        element = doc.findFirstElementByTagName("head", htmlElement);
        assertEquals(headElement, element);

        element = doc.findFirstElementByTagName("html", htmlElement);
        assertEquals(htmlElement, element);

        element = doc.findFirstElementByTagName("body", null);
        assertNull(element);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element htmlElement = doc.createElement("html");
        Element headElement = doc.createElement("head");
        Element titleElement = doc.createElement("title");
        titleElement.text("Test Title");
        Element bodyElement = doc.createElement("body");
        bodyElement.appendText("Hello World");

        htmlElement.appendChild(headElement);
        htmlElement.appendChild(titleElement);
        htmlElement.appendChild(bodyElement);

        doc.appendChild(htmlElement);

        String outerHtml = doc.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body>Hello World</body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element bodyElement = doc.createElement("body");
        bodyElement.text("Hello World");
        doc.appendChild(bodyElement);

        String text = doc.text();
        assertEquals("Hello World", text);
    }
}