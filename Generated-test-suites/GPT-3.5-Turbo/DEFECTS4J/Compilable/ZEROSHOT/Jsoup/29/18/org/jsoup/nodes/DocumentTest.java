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
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("head", shell.head().nodeName());
        assertEquals("body", shell.body().nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.appendElement("head").appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);

        document.appendElement("head").appendElement("title").text("Existing Title");
        document.title("Updated Title");
        title = document.title();
        assertEquals("Updated Title", title);
    }

    @Test
    public void testNormalise() {
        Element html = document.appendElement("html");
        html.appendElement("head").appendElement("title").text("Test Title");
        html.prependElement("div").appendText("Test Text");

        Document normalised = document.normalise();

        assertEquals("<html><head><title>Test Title</title></head><body><div>Test Text</div></body></html>", normalised.outerHtml());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testText() {
        document.appendElement("body");

        Element body = document.body();
        assertNotNull(body);

        document.text("Test Text");
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("head");
        document.appendElement("body");

        String outerHtml = document.outerHtml();
        assertEquals("<head></head><body></body>", outerHtml);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
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

        document.outputSettings(new Document.OutputSettings().prettyPrint(false));

        assertEquals(false, document.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
    }
}