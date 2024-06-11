package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        this.document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateEmptyDocument() {
        Document emptyDoc = new Document("http://example.com");
        assertNotNull(emptyDoc);
        assertEquals("#document", emptyDoc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document shellDoc = Document.createShell("http://example.com");
        assertNotNull(shellDoc);
        assertEquals("#document", shellDoc.nodeName());
        Element html = shellDoc.head();
        assertNotNull(html);
        Element head = html.head();
        assertNotNull(head);
        Element body = html.body();
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Element head = document.head();
        assertNull(head);
        document.title("Test Title");
        head = document.head();
        assertNotNull(head);
        assertEquals("Test Title", head.text());
    }

    @Test
    public void testGetBodyElement() {
        Element body = document.body();
        assertNull(body);
        document.title("Test Title");
        body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
    }

    @Test
    public void testNormalise() {
        document.title("Test Title 1");
        document.appendText("Test text 1");
        document.head().appendElement("head1");
        document.body().appendElement("body1");

        document.normalise();

        Element head = document.head();
        Element head1 = document.getElementsByTag("head").first();
        assertNotNull(head1);
        assertEquals(head, head1);

        Element body = document.body();
        Element body1 = document.getElementsByTag("body").first();
        assertNotNull(body1);
        assertEquals(body, body1);

        TextNode titleText = (TextNode) head.child(0);
        assertEquals("Test Title 1", titleText.text());

        TextNode bodyText = (TextNode) body.childNodes().get(0);
        assertEquals("Test text 1", bodyText.text());
    }

    @Test
    public void testOuterHtml() {
        document.title("Test Title 1");
        document.appendText("Test text 1");
        document.head().appendElement("head1");
        document.body().appendElement("body1");

        document.normalise();

        String html = document.outerHtml();
        String expectedHtml = "<html><head><title>Test Title 1</title>head1</head><body>Test text 1</body></html>";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testText() {
        document.title("Test Title 1");
        document.text("Test text 1");

        String text = document.text();
        assertEquals("Test text 1", text);
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertEquals(document.title(), clone.title());
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document documentWithOutputSettings = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        documentWithOutputSettings.outputSettings(outputSettings);

        String html = documentWithOutputSettings.html();
        String expectedHtml = "<html><head></head><body></body></html>";

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        html = documentWithOutputSettings.html();

        String extendedHtml = "<html><head></head><body></body></html>";
        assertEquals(extendedHtml, html);
    }

    @Test
    public void testOutputSettingsCharset() {
        Document documentWithOutputSettings = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        documentWithOutputSettings.outputSettings(outputSettings);

        String html = documentWithOutputSettings.html();
        String expectedHtml = "<html><head></head><body></body></html>";

        outputSettings.charset(StandardCharsets.ISO_8859_1);
        html = documentWithOutputSettings.html();

        String iso8859Html = "<html><head></head><body></body></html>";
        assertEquals(iso8859Html, html);
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document documentWithOutputSettings = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        documentWithOutputSettings.outputSettings(outputSettings);

        String html = documentWithOutputSettings.html();
        String expectedHtml = "<html><head></head><body></body></html>";

        outputSettings.prettyPrint(false);
        html = documentWithOutputSettings.html();

        String notPrettyPrintHtml = "<html><head></head><body></body></html>";
        assertEquals(notPrettyPrintHtml, html);
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document documentWithOutputSettings = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        documentWithOutputSettings.outputSettings(outputSettings);

        String html = documentWithOutputSettings.html();
        String expectedHtml = "<html><head></head><body></body></html>";

        outputSettings.indentAmount(4);
        html = documentWithOutputSettings.html();

        String indentedHtml = "<html>\n<head>\n</head>\n<body>\n</body>\n</html>";
        assertEquals(indentedHtml, html);
    }

    @Test
    public void testQuirksMode() {
        Document documentWithQuirksMode = new Document("http://example.com");
        documentWithQuirksMode.quirksMode(QuirksMode.quirks);

        String html = documentWithQuirksMode.html();
        String expectedHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<body>\n</body>\n</html>";

        assertEquals(expectedHtml, html);
    }
}