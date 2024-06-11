package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("https://www.example.com");
        assertNotNull(shellDoc);
        assertEquals("html", shellDoc.children().get(0).tagName());
        assertEquals("head", shellDoc.head().tagName());
        assertEquals("body", shellDoc.body().tagName());
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNull(headElement);
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNull(bodyElement);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleSet() {
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
    }

    @Test
    public void testClone() {
        Document cloneDoc = document.clone();
        assertNotSame(document, cloneDoc);
        assertEquals(document.baseUri(), cloneDoc.baseUri());
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertTrue(outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);
        assertEquals(newOutputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}