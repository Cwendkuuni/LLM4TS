package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", shell.html());
        assertEquals("http://example.com", shell.baseUri());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        Element head = document.appendElement("head");
        assertNotNull(document.head());
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        Element body = document.appendElement("body");
        assertNotNull(document.body());
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        Element titleElement = document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        OutputSettings settings = new OutputSettings();
        settings.escapeMode(OutputSettings.EscapeMode.extended);
        settings.charset("UTF-16");
        document.outputSettings(settings);
        
        assertEquals(settings, document.outputSettings());
        assertEquals(settings.escapeMode(), document.outputSettings().escapeMode());
        assertEquals(settings.charset(), document.outputSettings().charset());
        assertEquals(settings.prettyPrint(), document.outputSettings().prettyPrint());
        assertEquals(settings.indentAmount(), document.outputSettings().indentAmount());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Element htmlEl = document.appendElement("html");
        document.normalise();
        assertTrue(document.hasSameValue(htmlEl, document.child(0)));
        assertTrue(document.hasSameValue(htmlEl.child(0), document.child(0).child(0)));
        assertTrue(document.hasSameValue(htmlEl.child(1), document.child(0).child(1)));
        assertTrue(document.hasSameValue(htmlEl.child(0).child(0), document.head()));
        assertTrue(document.hasSameValue(htmlEl.child(1).child(0), document.body()));
    }

    @Test
    public void testText() {
        Element body = document.appendElement("body");
        document.text("Test Text");
        assertEquals("Test Text", document.text());
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div");
        assertEquals("<div></div>", document.outerHtml());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}