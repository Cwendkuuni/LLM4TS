package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        Element html = doc.head();
        assertEquals("head", html.tagName());
        html = doc.body();
        assertEquals("body", html.tagName());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertEquals(0, head.childElementsSize());
        assertEquals(0, body.childElementsSize());

        doc = Document.createShell("http://example.com");
        head = doc.head();
        body = doc.body();
        assertEquals(0, head.childElementsSize());
        assertEquals(0, body.childElementsSize());

        head.appendText("Test Title");
        assertEquals("Test Title", doc.title());

        body.appendElement("p").text("Test Text");
        assertEquals("Test Text", doc.body().text());

        doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().appendElement("p").text("Test Text");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1");
        doc.body().appendElement("p").text("Test Text");
        doc.appendText(" Test Text ");

        doc.normalise();
        Element meta = doc.head().getElementsByTag("meta").first();
        assertEquals("meta", meta.tagName());
        assertEquals("viewport", meta.attr("name"));
        assertEquals("width=device-width, initial-scale=1", meta.attr("content"));

        Element p = doc.body().getElementsByTag("p").first();
        assertEquals("p", p.tagName());
        assertEquals("Test Text", p.text());

        TextNode textNode = (TextNode) doc.getElementsByTag("textNode").first();
        assertEquals("Test Text", textNode.text());
        assertEquals(textNode.parent(), doc.body());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        doc.head = head;
        doc.body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", this);
        assertNull(title);

        title = doc.head.appendElement("title");
        assertEquals("title", title.tagName());
        title = doc.findFirstElementByTagName("title", this);
        assertEquals(title, title);

        title = doc.findFirstElementByTagName("body", this);
        assertNull(title);

        title = doc.body.appendElement("title");
        assertEquals("title", title.tagName());
        title = doc.findFirstElementByTagName("title", this);
        assertEquals(title, title);
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().appendElement("p").text("Test Text");

        Document clone = doc.clone();
        assertEquals("Test Title", doc.title());
        assertEquals("Test Text", doc.body().text());
        assertEquals("Test Title", clone.title());
        assertEquals("", clone.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        OutputSettings outputSettingsClone = doc.outputSettings().clone();
        assertEquals(outputSettings, outputSettingsClone);
    }
}