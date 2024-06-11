package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertEquals("html", shell.body().parent().tagName());
        assertEquals("head", shell.head().tagName());
        assertEquals("body", shell.body().tagName());
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
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.head().appendElement("title").text("Example Title");
        title = document.title();
        assertEquals("Example Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
        assertEquals("html", normalizedDoc.nodeName());
        assertEquals("head", normalizedDoc.head().tagName());
        assertEquals("body", normalizedDoc.body().tagName());
    }

    @Test
    public void testText() {
        document.text("Example Text");
        String text = document.body().text();
        assertEquals("Example Text", text);
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();
        assertTrue(html.isEmpty());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertTrue(outputSettings.prettyPrint());

        outputSettings = new Document.OutputSettings().prettyPrint(false);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.quirks);
        quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.quirks, quirksMode);
    }
}