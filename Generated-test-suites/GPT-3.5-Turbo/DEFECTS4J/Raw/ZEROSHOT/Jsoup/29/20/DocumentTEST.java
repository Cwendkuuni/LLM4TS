package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertEquals(Tag.valueOf("html"), shell.tagName());
        assertTrue(shell.hasElement("head"));
        assertTrue(shell.hasElement("body"));
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());

        document.title("Test Title");
        assertEquals("Test Title", document.title());

        // Add title to head
        Element head = document.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Head Title");
        assertEquals("Head Title", document.title());

        // Modify existing title
        document.title("Modified Title");
        assertEquals("Modified Title", document.title());
    }

    @Test
    public void testNormalise() {
        assertNull(document.findFirstElementByTagName("html", document));
        assertNull(document.head());
        assertNull(document.body());

        document.normalise();

        assertNotNull(document.findFirstElementByTagName("html", document));
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element element = new Element(Tag.valueOf("div"), "");
        element.appendChild(new TextNode("Text 1", ""));
        element.appendChild(new Comment("Comment"));
        element.appendChild(new TextNode("Text 2", ""));

        document.normaliseTextNodes(element);

        assertEquals(1, element.childNodes.size());
        assertTrue(element.childNodes.get(0) instanceof TextNode);
        assertEquals("Text 2", ((TextNode) element.childNodes.get(0)).text());
    }

    @Test
    public void testNormaliseStructure() {
        Element htmlEl = new Element(Tag.valueOf("html"), "");

        // Create multiple head elements
        Element head1 = htmlEl.appendElement("head");
        Element head2 = htmlEl.appendElement("head");
        Element head3 = htmlEl.appendElement("head");

        head1.appendElement("title").text("Title 1");
        head2.appendElement("title").text("Title 2");
        head3.appendElement("title").text("Title 3");

        document.normaliseStructure("head", htmlEl);

        assertEquals(1, htmlEl.getElementsByTag("head").size());
        assertEquals(3, document.getElementsByTag("title").size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element htmlEl = new Element(Tag.valueOf("html"), "");

        Element head = htmlEl.appendElement("head");
        head.appendElement("title").text("Title");

        Element body = htmlEl.appendElement("body");
        body.appendElement("div").text("Div");

        assertEquals(head, document.findFirstElementByTagName("head", htmlEl));
        assertEquals(body, document.findFirstElementByTagName("body", htmlEl));
        assertNull(document.findFirstElementByTagName("div", htmlEl));
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());

        Element htmlEl = document.appendElement("html");
        htmlEl.appendElement("head");
        htmlEl.appendElement("body");

        assertEquals("<html><head></head><body></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        assertNull(document.body());

        document.text("Test Text");
        assertNotNull(document.body());
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals(document.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());

        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(outputSettings, document.outputSettings());

        Document.OutputSettings newSettings = new Document.OutputSettings().charset(Charset.forName("ISO-8859-1"));
        document.outputSettings(newSettings);
        assertSame(newSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());

        document.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
    }
}