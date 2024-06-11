package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Document.java
 * @author YourName
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEmpty(doc.getElementsByTag("title"));
        assertFalse(doc.title().isEmpty());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.getElementsByTag("title").size());
        assertEquals(0, doc.title().length());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(head);
        assertNotNull(body);

        // Test getHead and getBody on an empty document
        Document emptyDoc = new Document(baseUri);
        Element emptyHead = emptyDoc.head();
        Element emptyBody = emptyDoc.body();
        assertNull(emptyHead);
        assertNull(emptyBody);
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element pElement = doc.createElement("p");
        assertEquals("p", pElement.nodeName());
        assertEquals(baseUri, pElement.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element p1 = doc.createElement("p").text("Test text 1");
        Element p2 = doc.createElement("p").text("Test text 2");

        doc.head().appendElement("p").text("Test text in head 1");
        doc.head().appendElement("p").text("Test text in head 2");
        doc.head().appendChild(p1);
        doc.head().appendChild(p2);

        doc.body().appendElement("p").text("Test text in body 1");
        doc.body().appendElement("p").text("Test text in body 2");
        doc.body().appendChild(p1);
        doc.body().appendChild(p2);

        doc.normalise();

        List<Element> bodyElements = doc.body().children();
        assertEquals(4, bodyElements.size());
        assertEquals("Test text in body 1", bodyElements.get(0).text());
        assertEquals("Test text in body 2", bodyElements.get(1).text());
        assertEquals("Test text 1", bodyElements.get(2).text());
        assertEquals("Test text 2", bodyElements.get(3).text());

        List<Element> headElements = doc.head().children();
        assertEquals(0, headElements.size());

        List<Element> titleElements = doc.getElementsByTag("title");
        assertEquals(1, titleElements.size());
        assertEquals("Test Title", titleElements.get(0).text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");

        Element p1 = doc.createElement("p").text("Test text 1");
        Element p2 = doc.createElement("p").text("Test text 2");

        doc.body().appendChild(p1);
        doc.body().appendChild(p2);

        doc.normalise();

        String expectedOuterHtml = "<html><head><title>Test Title</title></head><body>" +
                "<p>Test text 1</p>" +
                "<p>Test text 2</p>" +
                "</body></html>";

        assertEquals(expectedOuterHtml, doc.outerHtml());
    }
}