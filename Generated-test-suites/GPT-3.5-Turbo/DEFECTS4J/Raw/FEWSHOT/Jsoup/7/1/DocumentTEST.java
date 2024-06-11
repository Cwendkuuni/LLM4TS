package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals(3, doc.children().size());
        assertEquals("html", doc.children().get(0).tagName());
        assertEquals("head", doc.children().get(0).children().get(0).tagName());
        assertEquals("body", doc.children().get(0).children().get(1).tagName());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell();
        assertNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell();
        assertNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell();
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell();
        assertEquals(1, doc.children().size());
        assertEquals("html", doc.children().get(0).tagName());
        assertEquals(1, doc.children().get(0).children().size());
        assertEquals("head", doc.children().get(0).children().get(0).tagName());
        assertEquals(1, doc.children().get(0).children().get(0).children().size());
        assertEquals("body", doc.children().get(0).children().get(0).children().get(0).tagName());

        doc.normalise();
        assertEquals(1, doc.children().size());
        assertEquals("html", doc.children().get(0).tagName());
        assertEquals(1, doc.children().get(0).children().size());
        assertEquals("head", doc.children().get(0).children().get(0).tagName());
        assertEquals(1, doc.children().get(0).children().get(0).children().size());
        assertEquals("body", doc.children().get(0).children().get(0).children().get(0).tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell();
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell();
        doc.text("Test Text");
        assertEquals("Test Text", doc.text());

        doc.text(null);
        assertEquals("", doc.text());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = new Document().new OutputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = new Document().new OutputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());

        outputSettings.charset(Charset.forName("UTF-16"));
        assertEquals(Charset.forName("UTF-16"), outputSettings.charset());

        outputSettings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = new Document().new OutputSettings();
        assertEquals(true, outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = new Document().new OutputSettings();
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsGetter() {
        Document doc = Document.createShell();
        assertEquals(doc.new OutputSettings(), doc.outputSettings());
    }
}