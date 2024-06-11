package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertEquals(Tag.HTML, html.tagName());

        html = doc.body();
        assertEquals(Tag.BODY, html.tagName());

        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        doc.head() != null;
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        doc.body() != null;
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.setText("Test <html><head></head><body>Test</body></html>");

        doc.normalise();

        Element html = doc.head();
        assertNull(html);

        html = doc.body();
        TextNode textNode = (TextNode) html.getElementsByTag("text").first();
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertEquals(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertEquals(head, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertEquals(body, found);

        found = doc.findFirstElementByTagName("unknown", doc);
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        doc.setText("<html><head></head><body></body></html>");
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.setText("Test");
        assertEquals("Test", doc.text().toString());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }
}