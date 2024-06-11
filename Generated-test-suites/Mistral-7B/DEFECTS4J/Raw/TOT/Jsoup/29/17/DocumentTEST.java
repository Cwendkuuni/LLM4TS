package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Set up any initial state here if needed
    }

    @org.junit.Test
    public void testCreateDocument() {
        String baseUri = "http://example.com/";
        Document document = new Document(baseUri);
        assertNotNull(document);
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertEquals(baseUri, document.baseUri());

        Document shell = Document.createShell(baseUri);
        assertNotNull(shell);
        assertEquals(Tag.valueOf("html"), shell.tagName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @org.junit.Test
    public void testHeadAndBodyAccessors() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals(Tag.valueOf("head"), head.tagName());

        Element body = document.body();
        assertNotNull(body);
        assertEquals(Tag.valueOf("body"), body.tagName());
    }

    @org.junit.Test
    public void testTitleAccessor() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title("");
        assertNull(document.title());
    }

    @org.junit.Test
    public void testTitleSetter() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title(null);
        assertNull(document.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals(Tag.valueOf("a"), element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.text("Test <b>Text</b>");
        document.normalise();
        assertEquals("Test Text", document.text());

        Element html = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        html.appendChild(head);
        html.appendChild(body);
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        html.appendChild(document);
        document.normalise();
        assertEquals("Test Title", document.title());
    }

    @org.junit.Test
    public void testCreateShellWithTitle() {
        Document document = Document.createShell("http://example.com");
        Element title = document.getElementsByTag("title").first();
        assertNull(title);

        document.title("Test Title");
        title = document.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test Title", title.text());
    }

    @org.junit.Test
    public void testClone() {
        Document original = new Document("http://example.com");
        Document cloned = original.clone();
        assertNotNull(cloned);
        assertEquals(original.outputSettings(), cloned.outputSettings());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.createElement("title").text("Test Title");
        head.appendChild(title);

        Element found = document.findFirstElementByTagName("title", this);
        assertNotNull(found);
        assertEquals(title, found);

        found = document.findFirstElementByTagName("body", this);
        assertNotNull(found);
        assertEquals(body, found);

        found = document.findFirstElementByTagName("script", this);
        assertNull(found);
    }

    @org.junit.Test
    public void testOuterHtmlAndText() {
        Document document = new Document("http://example.com");
        document.text("Test <b>Text</b>");
        assertEquals("<html><head><base href=\"http://example.com\"><title></title></head><body><p>Test <b>Text</b></p></body></html>", document.outerHtml());
        assertEquals("Test Text", document.text());
    }

    @org.junit.Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entries.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset(Charset.forName("ISO-8859-1")).prettyPrint(false).indentAmount(5);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.charset());
        assertEquals(false, newOutputSettings.prettyPrint());
        assertEquals(5, newOutputSettings.indentAmount());
    }
}