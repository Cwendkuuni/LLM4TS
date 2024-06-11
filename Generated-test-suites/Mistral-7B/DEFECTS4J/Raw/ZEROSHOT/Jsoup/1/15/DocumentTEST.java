package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocumentWithBaseUri() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetDocumentTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");

        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetDocumentTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");

        String originalTitle = doc.title();
        doc.title("New Title");

        String newTitle = doc.title();
        assertEquals("New Title", newTitle);
        assertNotEquals(originalTitle, newTitle);
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element newElement = doc.createElement("p");

        assertEquals("p", newElement.tagName());
        assertEquals("http://example.com", newElement.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("This is a test document.");
        Element pElement = doc.createElement("p");
        pElement.text("This is a test paragraph.");
        doc.body().appendChild(pElement);

        doc.normalise();

        Element titleElement = doc.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());

        Element bodyElement = doc.body();
        assertNotNull(bodyElement);
        assertEquals("This is a test document.", bodyElement.text());
        assertEquals(1, bodyElement.childNodes().size());
        assertEquals("p", bodyElement.childNodes().get(0).tagName());
        assertEquals("This is a test paragraph.", bodyElement.childNodes().get(0).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("This is a test document.");
        Element pElement = doc.createElement("p");
        pElement.text("This is a test paragraph.");
        doc.body().appendChild(pElement);

        doc.normalise();

        String outerHtml = doc.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body>This is a test document.<p>This is a test paragraph.</p></body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }
}