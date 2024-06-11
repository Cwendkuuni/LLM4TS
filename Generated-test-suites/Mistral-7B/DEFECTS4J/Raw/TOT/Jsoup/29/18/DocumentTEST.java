package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());

        Document shell = Document.createShell("http://example.com");
        assertEquals("#document", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testGetAndSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title(null);
        assertEmpty(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertNull(element.parent());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.appendElement("title").text("Test Title");
        Element body = doc.appendElement("body").text("Test Body");
        Element head = doc.head();
        head.appendElement("meta").attr("charset", "UTF-8");

        doc.normalise();

        assertEquals(3, doc.childNodes().size());
        assertEquals("#document", doc.nodeName());
        assertEquals("html", doc.child(0).tagName());
        assertNotNull(doc.child(0).firstChild());
        assertEquals("head", doc.child(0).firstChild().nodeName());
        assertEquals("meta", doc.child(0).firstChild().child(0).nodeName());
        assertEquals("charset", doc.child(0).firstChild().child(0).attr("charset"));
        assertEquals("body", doc.child(1).tagName());
        assertEquals("Test Body", doc.child(1).text());
        assertEquals("title", doc.child(2).tagName());
        assertEquals("Test Title", doc.child(2).child(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element headTag = doc.findFirstElementByTagName("head", this);
        assertEquals(head, headTag);

        Element bodyTag = doc.findFirstElementByTagName("body", this);
        assertEquals(body, bodyTag);

        Element nonExistentTag = doc.findFirstElementByTagName("nonExistent", this);
        assertNull(nonExistentTag);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = new OutputSettings();
        newOutputSettings.escapeMode(Entities.EscapeMode.extended);
        newOutputSettings.charset("ISO-8859-1");
        newOutputSettings.prettyPrint(false);
        newOutputSettings.indentAmount(2);

        doc.outputSettings(newOutputSettings);
        assertNotEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());
        assertNotEquals("UTF-8", doc.outputSettings().charset().name());
        assertFalse(doc.outputSettings().prettyPrint());
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}