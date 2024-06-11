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
    public void testHead() {
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitleWhenNoTitleElementExists() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWhenTitleElementExists() {
        document.head().appendElement("title").text("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleWithNullTitle() {
        document.title(null);
    }

    @Test
    public void testSetTitleWhenTitleElementExists() {
        document.head().appendElement("title").text("Old Title");
        document.title("New Title");

        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.nodeName());
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testSetTitleWhenNoTitleElementExists() {
        document.title("New Title");

        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.nodeName());
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.normalise();
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals(document.html(), outerHtml);
    }

    @Test
    public void testText() {
        Document documentWithText = document.text("Test Text");

        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
        assertEquals("Test Text", bodyElement.text());

        assertEquals(document, documentWithText);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals("UTF-8", outputSettings.charset().name());

        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
}