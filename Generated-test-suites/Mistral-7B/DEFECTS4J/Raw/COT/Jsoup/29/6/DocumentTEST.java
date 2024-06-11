package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.jsoup.nodes.TextNode;

/**
 * Test class for Document class
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
        Element root = doc.childNodes().first();
        assertEquals("html", root.tagName());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.getFirstElementSibling();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.nextElementSibling();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testTitleAccessors() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("");
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
        assertEquals("", p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element titleEl = doc.appendElement("title").text("Test Title");
        Element h1 = doc.createElement("h1").text("Test Heading");
        doc.body().appendElement("p").text("Test Paragraph");
        doc.head().appendChild(titleEl);
        doc.body().appendChild(h1);

        doc.normalise();

        Element title = doc.head().getFirstElementByTagName("title");
        assertNotNull(title);
        assertEquals("Test Title", title.text());
        Element body = doc.body();
        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }
        assertEquals(3, textNodes.size());
        String expectedText = "Test Paragraph\nTest Heading\n";
        TextNode firstTextNode = (TextNode) textNodes.get(0);
        assertEquals(expectedText, firstTextNodes.getData());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document doc = new Document("");
        findFirstElementByTagName("title", null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title").text("Test Title");
        Element body = doc.createElement("body");
        Element h1 = doc.createElement("h1").text("Test Heading");

        html.appendChild(head);
        head.appendChild(title);
        html.appendChild(body);
        body.appendChild(h1);

        Element foundTitle = doc.findFirstElementByTagName("title");
        assertNotNull(foundTitle);
        assertEquals("title", foundTitle.tagName());
        assertEquals("Test Title", foundTitle.text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element title = doc.createElement("title").text("Test Title");
        Element h1 = doc.createElement("h1").text("Test Heading");
        doc.body().appendChild(title);
        doc.body().appendChild(h1);

        doc.head().appendElement("meta").attr("charset", "UTF-8");

        String html = doc.outerHtml();
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body><title>Test Title</title><h1>Test Heading</h1></body></html>";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element title = doc.createElement("title").text("Test Title");
        Element body = doc.createElement("body").text("Test Paragraph");
        doc.appendChild(title);
        doc.appendChild(body);

        String text = doc.text();
        String expectedText = "Test ParagraphTest Title";
        assertEquals(expectedText, text);
    }

    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element title = doc.createElement("title").text("Test Title");
        doc.head().appendChild(title);

        OutputSettings outputSettings = doc.outputSettings();
        OutputSettings clonedOutputSettings = outputSettings.clone();
        assertEquals(outputSettings, clonedOutputSettings);

        Document clonedDoc = doc.clone();
        assertEquals(doc.title(), clonedDoc.title());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);

        String input = "Test <a href=\"http://example.com\">Link</a>";
        String expectedOutput = "Test &lt;a href=&quot;http://example.com&quot;&gt;Link&lt;/a&gt;";
        String output = doc.outerHtml(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOutputSettingsCharset() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Charset inputCharset = Charset.forName("ISO-8859-1");
        doc.outputSettings().charset(inputCharset);

        String input = "Test é";
        String expectedOutput = "Test é";
        String output = doc.text(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputSettingsInvalidCharset() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().charset("Invalid Charset");
    }

    @Test
    public void testPrettyPrint() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().prettyPrint(false);
        String input = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String expectedOutput = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String output = doc.outerHtml(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testIndentAmount() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().indentAmount(2);
        String input = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String expectedOutput = "  <html>\n  <head>\n    <title>Test Title</title>\n  </head>\n  <body>\n    <h1>Test Heading</h1>\n  </body>\n</html>\n";
        String output = doc.outerHtml(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testQuirksMode() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.quirksMode(QuirksMode.quirks);
        String input = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<h1>Test Heading</h1>\n</body>\n</html>";
        String output = doc.outerHtml(input);
        assertEquals(expectedOutput, output);
    }
}