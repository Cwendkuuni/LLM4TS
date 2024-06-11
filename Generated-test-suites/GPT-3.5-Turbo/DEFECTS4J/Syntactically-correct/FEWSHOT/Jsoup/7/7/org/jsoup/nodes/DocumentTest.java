package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
        doc.head().appendElement("title").text("Test Title");
        assertEquals("title", doc.head().nodeName());
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
        doc.body().appendElement("div").text("Test Body");
        assertEquals("div", doc.body().nodeName());
        assertEquals("Test Body", doc.body().text());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("div").text("Test Div");
        doc.head().appendElement("script").attr("src", "script.js");
        doc.normalise();
        assertNull(doc.head().getElementsByTag("script").first());
        assertEquals("Test Div", doc.body().text());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element div = doc.createElement("div");
        div.attr("id", "testId");
        assertEquals("div", div.nodeName());
        assertEquals("testId", div.attr("id"));
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.body().text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        assertEquals("base", settings.escapeMode().toString());
        assertEquals("UTF-8", settings.charset().name());
        assertTrue(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());

        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals("extended", settings.escapeMode().toString());

        settings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", settings.charset().name());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());

        settings.indentAmount(2);
        assertEquals(2, settings.indentAmount());
    }
}