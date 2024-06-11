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
        assertEquals("html", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
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
    public void testSetTitle() {
        document.title("Example Title");
        String title = document.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testNormalise() {
        Document normalisedDocument = document.normalise();
        assertEquals(document, normalisedDocument);
    }

    @Test
    public void testNormaliseTextNodes() {
        document.normaliseTextNodes(document);
        // No assertions as the method only modifies the document structure.
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element element = document.createElement("div");
        Element firstElement = document.findFirstElementByTagName("div", element);
        assertEquals(element, firstElement);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Example Text");
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testEscapeMode() {
        Entities.EscapeMode escapeMode = document.outputSettings().escapeMode();
        assertEquals(Entities.EscapeMode.base, escapeMode);
    }

    @Test
    public void testSetEscapeMode() {
        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        Entities.EscapeMode escapeMode = document.outputSettings().escapeMode();
        assertEquals(Entities.EscapeMode.extended, escapeMode);
    }

    @Test
    public void testCharset() {
        Charset charset = document.outputSettings().charset();
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testSetCharset() {
        document.outputSettings().charset(Charset.forName("ISO-8859-1"));
        Charset charset = document.outputSettings().charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testSetCharsetByName() {
        document.outputSettings().charset("ISO-8859-1");
        Charset charset = document.outputSettings().charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testPrettyPrint() {
        boolean prettyPrint = document.outputSettings().prettyPrint();
        assertTrue(prettyPrint);
    }

    @Test
    public void testSetPrettyPrint() {
        document.outputSettings().prettyPrint(false);
        boolean prettyPrint = document.outputSettings().prettyPrint();
        assertFalse(prettyPrint);
    }

    @Test
    public void testIndentAmount() {
        int indentAmount = document.outputSettings().indentAmount();
        assertEquals(1, indentAmount);
    }

    @Test
    public void testSetIndentAmount() {
        document.outputSettings().indentAmount(4);
        int indentAmount = document.outputSettings().indentAmount();
        assertEquals(4, indentAmount);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
}