package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleMethod() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("baseUri");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodNullTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.appendElement("p").text("Test text");
        doc.appendElement("div").text("Another test text");

        doc.normalise();

        Element body = doc.body();
        List<Element> pElements = doc.select("p");
        List<Element> divElements = doc.select("div");

        assertTrue(pElements.size() > 0);
        assertTrue(divElements.size() > 0);
        assertEquals(body.text(), "Test text");
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendElement("p").text("Test text");

        String outerHtml = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test text</p></body>", outerHtml);
    }

    @Test
    public void testTextMethod() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.appendElement("html").appendElement("body").text("Test text");

        doc.text("New test text");

        assertEquals("New test text", doc.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodEmptyTitle() {
        Document doc = new Document("baseUri");
        doc.title("");
    }
}