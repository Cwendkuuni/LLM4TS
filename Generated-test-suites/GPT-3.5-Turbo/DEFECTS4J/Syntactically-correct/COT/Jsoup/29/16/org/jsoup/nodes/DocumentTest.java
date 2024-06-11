package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleWithTitleElement() {
        document.head().appendElement("title").text("Example Title");
        String title = document.title();
        assertNotNull(title);
        assertEquals("Example Title", title);
    }

    @Test
    public void testTitleWithoutTitleElement() {
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testSetTitleWithTitleElement() {
        document.title("New Title");
        String title = document.title();
        assertNotNull(title);
        assertEquals("New Title", title);
    }

    @Test
    public void testSetTitleWithoutTitleElement() {
        document.head().removeChild(document.head().getElementsByTag("title").first());
        document.title("New Title");
        String title = document.title();
        assertNotNull(title);
        assertEquals("New Title", title);
    }

    @Test
    public void testAddElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        Element htmlEl = document.getElementsByTag("html").first();
        assertNotNull(htmlEl);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testText() {
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div").text("Example Text");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("<div>Example Text</div>", outerHtml);
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.outputSettings().charset().name(), clone.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
    }
}