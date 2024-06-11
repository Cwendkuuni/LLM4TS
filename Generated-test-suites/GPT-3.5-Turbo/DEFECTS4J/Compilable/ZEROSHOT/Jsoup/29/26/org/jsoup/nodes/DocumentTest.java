package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {
    private Document doc;

    @Before
    public void setUp() {
        doc = new Document("http://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://www.example.com");
        assertNotNull(shell);
        assertEquals("html", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = doc.title();
        assertNotNull(title);
    }

    @Test
    public void testSetTitle() {
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document normalisedDoc = doc.normalise();
        assertNotNull(normalisedDoc);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = doc.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        doc.text("Test text");
        String text = doc.text();
        assertEquals("Test text", text);
    }

    @Test
    public void testNodeName() {
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testClone() {
        Document clone = doc.clone();
        assertNotNull(clone);
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
    }

    @Test
    public void testSetOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertNotNull(quirksMode);
    }

    @Test
    public void testSetQuirksMode() {
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}