package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * JUnit test cases for the Document class
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadAccessors() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        assertEquals("head", head.nodeName());
        assertEquals("baseUri", head.baseUri());
        assertNull(head.text());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", head.text().trim());
    }

    @Test
    public void testBodyAccessors() {
        Document doc = Document.createShell("baseUri");
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("baseUri", body.baseUri());
        assertNull(body.text());
        doc.title("Test Title");
        doc.text("Test Body");
        assertEquals("Test Body", body.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element element = doc.createElement("a");
        assertEquals("a", element.nodeName());
        assertEquals("baseUri", element.baseUri());
        assertNull(element.parent());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.head();
        Element body = doc.body();
        doc.title("Test Title");
        head.appendElement("title").text("Another Title");
        TextNode textNode = doc.createElement("textnode").text("Test Text");
        body.appendElement("p").text("Test Paragraph");
        body.appendChild(textNode);

        doc.normalise();

        Element htmlEl = doc.findFirstElementByTagName("html", this);
        assertEquals(1, htmlEl.childNodes().size());
        assertEquals(3, htmlEl.childNodes().get(0).childNodes().size());
        assertEquals("Test Title", htmlEl.childNodes().get(0).childNodes().get(0).childNodes().get(0).text().trim());
        assertEquals("head", htmlEl.childNodes().get(0).childNodes().get(0).nodeName());
        assertEquals("body", htmlEl.childNodes().get(0).childNodes().get(1).nodeName());
        assertEquals("Test Paragraph", htmlEl.childNodes().get(0).childNodes().get(1).childNodes().get(0).text().trim());
        assertEquals("textnode", htmlEl.childNodes().get(0).childNodes().get(1).childNodes().get(1).nodeName());
        assertEquals("Test Text", htmlEl.childNodes().get(0).childNodes().get(1).childNodes().get(1).childNodes().get(0).text().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseWithNullDocument() {
        Document.normalise(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element htmlEl = doc.findFirstElementByTagName("html", this);
        assertEquals("html", htmlEl.nodeName());
        assertEquals(1, htmlEl.childNodes().size());
        Element headEl = htmlEl.childNodes().get(0);
        assertEquals("head", headEl.nodeName());
        assertEquals(1, headEl.childNodes().size());
        assertEquals("title", headEl.childNodes().get(0).nodeName());
        Element bodyEl = htmlEl.childNodes().get(1);
        assertEquals("body", bodyEl.nodeName());
        assertEquals(0, bodyEl.childNodes().size());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element htmlEl = doc.appendElement("html");
        Element headEl = doc.appendElement("head", htmlEl);
        Element titleEl = doc.appendElement("title", headEl);
        titleEl.text("Test Title");
        Element bodyEl = doc.appendElement("body", htmlEl);
        bodyEl.appendElement("p").text("Test Paragraph");

        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        Element bodyEl = doc.body();
        bodyEl.text("Test Text");
        assertEquals("Test Text", doc.text().trim());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("baseUri");
        Document clonedDoc = doc.clone();
        assertEquals(doc.outputSettings(), clonedDoc.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("baseUri");
        OutputSettings settings = doc.outputSettings();
        settings.escapeMode(OutputSettings.Entities.EscapeMode.extended);
        assertEquals(OutputSettings.Entities.EscapeMode.extended, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().name());
        assertEquals(StandardCharsets.UTF_8, settings.encoder().encoding());
    }
}