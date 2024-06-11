package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEmpty(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        Element html = doc.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);

        doc = Document.createShell("baseUri");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);

        doc = Document.createShell("baseUri");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitleElement() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());

        doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitleElement() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        doc.body().appendText("Text in root");
        doc.head().appendText("Text in head");
        doc.setText("Text in body");

        doc.normalise();

        assertEquals("Text in body", doc.body().text());
        assertEquals("", doc.head().text());
        assertEquals("Text in body", doc.text().toString());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        doc.body().appendText("Text in body");

        assertEquals("<html><head></head><body>Text in body</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextNode() {
        Document doc = Document.createShell("baseUri");
        TextNode textNode = doc.createElement("textNode").text("Test Text");
        doc.body().appendChild(textNode);

        assertEquals("Test Text", doc.body().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new Document(null);
    }
}