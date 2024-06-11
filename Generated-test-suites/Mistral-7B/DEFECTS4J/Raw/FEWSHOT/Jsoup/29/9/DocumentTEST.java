package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("baseUri");
        assertEquals(Tag.HTML, doc.tag());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals(Tag.HTML, doc.tag());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(head);
        assertNotNull(body);
        assertEquals(Tag.HEAD, head.tag());
        assertEquals(Tag.BODY, body.tag());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.title("New Test Title");
        assertEquals("New Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element element = doc.createElement("a");
        assertEquals(Tag.A, element.tag());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.body().appendText("Test Text");
        doc.body().appendElement(doc.createElement("p")).appendText("Test Paragraph");

        doc.normalise();

        assertEquals("Test Title", doc.title());
        assertEquals("Test TextTest Paragraph", doc.body().text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.findFirstElementByTagName(Tag.HTML.name(), doc);
        assertSame(doc, html.parent());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.body().appendText("Test Text");
        doc.body().appendElement(doc.createElement("p")).appendText("Test Paragraph");

        assertEquals("<html><head></head><body>" +
                "<title>Test Title</title>" +
                "</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        doc.body().appendText("Test Text");

        assertEquals("Test Text", doc.text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.body().appendText("Test Text");

        Document clone = doc.clone();
        assertEquals("Test Title", clone.title());
        assertEquals("Test Text", clone.body().text());
    }
}