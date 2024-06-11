package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("baseUri", doc.baseUri());
        assertTrue(doc.head() == null);
        assertTrue(doc.body() == null);
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        Element html = doc.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("baseUri", doc.baseUri());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc.head());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc.body());
    }

    @Test
    public void testGetTitleElement() {
        Document doc = Document.createShell("baseUri");
        Element titleElement = doc.titleElement(); // no getTitle() method in Document class
        assertNull(titleElement);
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("baseUri");
        doc.head().appendElement("p").text("Test");
        doc.body().appendElement("p").text("Test");
        doc.title("Test Title");

        doc.normalise();

        List<Element> bodyElements = doc.select("body > *");
        assertEquals(3, bodyElements.size());
        Element titleElement = doc.select("head > title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        doc.head().appendElement("p").text("Test");
        doc.body().appendElement("p").text("Test");
        doc.title("Test Title");

        String outerHtml = doc.outerHtml();
        assertTrue(outerHtml.contains("<html><head><title>Test Title</title></head><body>"));
        assertTrue(outerHtml.contains("</body>"));
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        doc.body().appendElement("p").text("Test");

        doc.text("New Text");

        assertEquals("New Text", doc.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        Document doc = new Document("baseUri");
        doc.createElement(null);
    }
}