package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

public class DocumentTest {
    private Document doc;

    @Before
    public void setup() {
        doc = new Document("");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.nodeName());
    }

    @Test
    public void testHead() {
        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        Element titleEl = new Element(Tag.valueOf("title"), "");
        titleEl.text("Existing Title");
        doc.body().appendChild(titleEl);

        doc.title("New Title");
        String title = doc.title();

        assertEquals("New Title", title);
        assertEquals(1, doc.getElementsByTag("title").size());
        assertEquals("New Title", doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitleWithNullTitleElement() {
        doc.body().appendChild(new Element(Tag.valueOf("div"), ""));

        doc.title("New Title");
        String title = doc.title();

        assertEquals("New Title", title);
        assertEquals(1, doc.getElementsByTag("title").size());
        assertEquals("New Title", doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitleWithNullTitle() {
        doc.title(null);
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Element div = doc.createElement("div");
        assertNotNull(div);
        assertEquals("div", div.nodeName());
    }

    @Test
    public void testNormalise() {
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.nodeName());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        div.appendChild(new TextNode("Text 1", ""));
        div.appendChild(new TextNode("Text 2", ""));
        div.appendChild(new Comment("Comment"));

        doc.normalise();

        assertEquals(2, doc.body().childNodeSize());
        assertTrue(doc.body().childNode(0) instanceof TextNode);
        assertTrue(doc.body().childNode(1) instanceof TextNode);
        assertEquals("Text 1", doc.body().childNode(0).outerHtml().trim());
        assertEquals("Text 2", doc.body().childNode(1).outerHtml().trim());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        Element divFound = doc.findFirstElementByTagName("div", doc);
        assertEquals(div, divFound);
    }

    @Test
    public void testOuterHtml() {
        doc.body().text("Test Content");
        String outerHtml = doc.outerHtml();
        assertEquals("Test Content", outerHtml);
    }

    @Test
    public void testText() {
        doc.text("Test Content");
        String textContent = doc.text();
        assertEquals("Test Content", textContent);
    }

    @Test
    public void testNodeName() {
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertEquals(Entities.EscapeMode.extended, escapeMode);
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.charset(Charset.forName("UTF-16"));
        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("UTF-16"), charset);
    }

    @Test
    public void testOutputSettingsCharsetByName() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.charset("ISO-8859-1");
        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.prettyPrint(false);
        boolean prettyPrint = outputSettings.prettyPrint();
        assertEquals(false, prettyPrint);
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.indentAmount(4);
        int indentAmount = outputSettings.indentAmount();
        assertEquals(4, indentAmount);
    }
}