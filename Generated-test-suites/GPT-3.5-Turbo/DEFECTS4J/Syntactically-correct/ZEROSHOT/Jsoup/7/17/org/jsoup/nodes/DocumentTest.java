package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        // Test if createShell method creates a valid shell of a document
        Document doc = Document.createShell("https://example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testHead() {
        // Test if head() method returns the correct head element of the document
        Document doc = new Document("https://example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        // Test if body() method returns the correct body element of the document
        Document doc = new Document("https://example.com");
        Element body = doc.body();
        assertNull(body);

        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        // Test if title() method returns the correct title of the document
        Document doc = new Document("https://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.appendElement("title").text("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        // Test if title() method sets the correct title of the document
        Document doc = new Document("https://example.com");
        doc.title("Test Title");

        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        // Test if createElement() method creates a new element with the correct tag name
        Document doc = new Document("https://example.com");
        Element element = doc.createElement("div");

        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        // Test if normalise() method normalizes the document correctly
        Document doc = Document.createShell("https://example.com");
        doc.appendElement("head").text("This is the head");
        doc.appendElement("body").text("This is the body");

        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        Element textNode = (Element) body.childNodes().get(0);

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals(0, head.childNodes().size());
        assertEquals(0, html.childNodes().size());
        assertEquals("This is the head", head.text());
        assertEquals("This is the body", textNode.text());
    }

    @Test
    public void testOuterHtml() {
        // Test if outerHtml() method returns the correct outer HTML of the document
        Document doc = Document.createShell("https://example.com");
        String outerHtml = doc.outerHtml();

        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        // Test if text() method sets the correct text of the body element
        Document doc = Document.createShell("https://example.com");
        doc.text("Test Text");

        Element body = doc.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testNodeName() {
        // Test if nodeName() method returns the correct node name of the document
        Document doc = Document.createShell("https://example.com");
        String nodeName = doc.nodeName();

        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        // Test if outputSettings() method returns the correct output settings of the document
        Document doc = Document.createShell("https://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }
}