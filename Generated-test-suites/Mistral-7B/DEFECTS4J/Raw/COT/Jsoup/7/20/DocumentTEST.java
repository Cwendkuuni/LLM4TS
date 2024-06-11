package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class
 */
public class DocumentTest {

    /**
     * Test constructor Document
     */
    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    /**
     * Test method createShell
     */
    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test method head
     */
    @org.junit.Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    /**
     * Test method body
     */
    @org.junit.Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test method title
     */
    @org.junit.Test
    public void testTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
        document.title("title");
        title = document.title();
        assertEquals("title", title);
    }

    /**
     * Test method title with null title
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTitleWithNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    /**
     * Test method createElement
     */
    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    /**
     * Test method normalise
     */
    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("title");
        Element body = html.appendElement("body");
        body.text("text body");
        document.normalise();
        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }
        assertEquals(1, textNodes.size());
        assertEquals("text body", textNodes.get(0).text());
        assertEquals("title", title.text().trim());
    }

    /**
     * Test method findFirstElementByTagName
     */
    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("title");
        Element title1 = document.findFirstElementByTagName("title", this);
        assertEquals(title, title1);
        Element body1 = document.findFirstElementByTagName("body", this);
        assertEquals(body, body1);
        Element head1 = document.findFirstElementByTagName("head", this);
        assertEquals(html, head1.parent());
    }

    /**
     * Test method outerHtml
     */
    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("title");
        Element body = html.appendElement("body");
        body.text("text body");
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.contains("<html><head><title>title</title>"));
        assertTrue(outerHtml.contains("<body>text body</body>"));
    }

    /**
     * Test method text
     */
    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("text body");
        String text = document.text();
        assertEquals("text body", text);
    }

    /**
     * Test method nodeName
     */
    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    /**
     * Test method OutputSettings constructor
     */
    @org.junit.Test
    public void testOutputSettingsConstructor() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    /**
     * Test method escapeMode with base
     */
    @org.junit.Test
    public void testOutputSettingsEscapeModeBase() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.base);
        assertEquals(Entities.EscapeMode.base, newOutputSettings.escapeMode());
        document.outputSettings(newOutputSettings);
        String text = document.text("text with <script> tag");
        assertEquals("text with &lt;script&gt; tag", text);
    }

    /**
     * Test method escapeMode with extended
     */
    @org.junit.Test
    public void testOutputSettingsEscapeModeExtended() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        document.outputSettings(newOutputSettings);
        String text = document.text("text with <script> tag");
        assertEquals("text with <script> tag", text);
    }

    /**
     * Test method charset with UTF-8
     */
    @org.junit.Test
    public void testOutputSettingsCharsetUTF8() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.charset(Charset.forName("UTF-16BE"));
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        document.outputSettings(newOutputSettings);
        assertEquals(Charset.forName("UTF-16BE"), document.outputSettings().charset());
    }

    /**
     * Test method charset with other charset
     */
    @org.junit.Test
    public void testOutputSettingsCharsetOther() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.charset(Charset.forName("UTF-16LE"));
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        document.outputSettings(newOutputSettings);
        assertEquals(Charset.forName("UTF-16LE"), document.outputSettings().charset());
    }

    /**
     * Test method prettyPrint with true
     */
    @org.junit.Test
    public void testOutputSettingsPrettyPrintTrue() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.prettyPrint(false);
        assertEquals(true, outputSettings.prettyPrint());
        document.outputSettings(newOutputSettings);
        assertEquals(false, document.outputSettings().prettyPrint());
    }

    /**
     * Test method prettyPrint with false
     */
    @org.junit.Test
    public void testOutputSettingsPrettyPrintFalse() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.prettyPrint(true);
        assertEquals(false, outputSettings.prettyPrint());
        document.outputSettings(newOutputSettings);
        assertEquals(true, document.outputSettings().prettyPrint());
    }

    /**
     * Test method indentAmount with 1
     */
    @org.junit.Test
    public void testOutputSettingsIndentAmount1() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.indentAmount(2);
        assertEquals(1, outputSettings.indentAmount());
        document.outputSettings(newOutputSettings);
        assertEquals(2, document.outputSettings().indentAmount());
    }

    /**
     * Test method indentAmount with other value
     */
    @org.junit.Test
    public void testOutputSettingsIndentAmountOther() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = outputSettings.indentAmount(3);
        assertEquals(1, outputSettings.indentAmount());
        document.outputSettings(newOutputSettings);
        assertEquals(3, document.outputSettings().indentAmount());
    }
}