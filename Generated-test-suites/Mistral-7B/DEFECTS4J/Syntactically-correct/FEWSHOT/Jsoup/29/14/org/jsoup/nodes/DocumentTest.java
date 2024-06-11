package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body Text ");
        Element titleEl = doc.createElement("title").text("New Title");
        doc.head().appendElement(titleEl);
        Element bodyEl = doc.createElement("body").text("New Body Text ");
        doc.replaceElements(doc.body(), bodyEl);

        Document normalisedDoc = doc.normalise();
        assertEquals("Test Title", normalisedDoc.title());
        assertEquals("New Body Text ", normalisedDoc.body().text());
        assertEquals(1, normalisedDoc.getElementsByTag("title").size());
        assertEquals(1, normalisedDoc.getElementsByTag("body").size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.findFirstElementByTagName("html", doc);
        assertNotNull(htmlEl);
        assertEquals("html", htmlEl.nodeName());

        Element titleEl = doc.findFirstElementByTagName("title", doc);
        assertNull(titleEl);

        doc.title("Test Title");
        titleEl = doc.findFirstElementByTagName("title", doc);
        assertNotNull(titleEl);
        assertEquals("title", titleEl.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body Text ");

        String html = doc.outerHtml();
        assertTrue(html.contains("<html"));
        assertTrue(html.contains("<head"));
        assertTrue(html.contains("<title>Test Title</title>"));
        assertTrue(html.contains("<body>Test Body Text </body>"));
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Test Body Text ");

        Element body = doc.text("Test Text");
        assertEquals("Test Text", body.text());
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");

        Document clone = doc.clone();
        assertEquals("Test Title", clone.title());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        Charset charset = Charset.forName("UTF-16");
        doc.outputSettings(outputSettings.charset(charset));
        assertEquals(charset.name(), doc.outputSettings().charset().name());
    }
}