package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        assertEquals(Tag.HTML, html.tagName());
        assertSame(doc, html.parent());

        html = doc.body();
        assertEquals(Tag.BODY, html.tagName());
        assertSame(doc, html.parent());

        assertEquals(3, doc.childNodeSize());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        assertEquals(Tag.P, p.tagName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.appendChild(new TextNode("Test 1"));
        doc.head().appendChild(new TextNode("Test 2"));
        doc.head().appendChild(new TextNode("Test 3"));

        doc.normalise();
        assertEquals(3, doc.childNodeSize());
        assertEquals(Tag.BODY, doc.getFirstChild().tagName());
        assertEquals("Test 1", doc.getFirstChild().text());
        assertEquals(Tag.HEAD, doc.getFirstChild().nextSibling().tagName());
        assertEquals("", doc.getFirstChild().nextSibling().text());
        assertEquals(Tag.TITLE, doc.getFirstChild().nextSibling().nextSibling().tagName());
        assertEquals("Test 2Test 3", doc.getFirstChild().nextSibling().nextSibling().text());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.BASE, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings settings = outputSettings.escapeMode(Entities.EscapeMode.EXTENDED);
        assertEquals(Entities.EscapeMode.EXTENDED, settings.escapeMode());
        assertEquals(outputSettings, settings);

        settings = outputSettings.charset(Charset.forName("UTF-16"));
        assertEquals(Charset.forName("UTF-16"), settings.charset());
        assertEquals(outputSettings, settings);

        settings = outputSettings.charset("UTF-16");
        assertEquals(Charset.forName("UTF-16"), settings.charset());
        assertEquals(outputSettings, settings);

        settings = outputSettings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
        assertEquals(outputSettings, settings);

        settings = outputSettings.indentAmount(3);
        assertEquals(3, settings.indentAmount());
        assertEquals(outputSettings, settings);
    }
}