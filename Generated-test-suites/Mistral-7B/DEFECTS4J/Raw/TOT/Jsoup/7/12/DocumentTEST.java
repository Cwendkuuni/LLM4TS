package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.appendElement("p").text("Test Paragraph");

        Element head = doc.head();
        Element body = doc.body();

        assertEquals("Test Title", doc.title());
        assertEquals("", doc.title());
        assertNotNull(head);
        assertNotNull(body);
        assertFalse(head.hasText());
        assertFalse(body.hasText());

        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());
        Element p = body.getElementsByTag("p").first();
        assertEquals("Test Paragraph", p.text().trim());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");

        doc.title("Test Title 1");
        assertEquals("Test Title 1", doc.title());

        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("", element.text().trim());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendElement("p").text("Test Paragraph");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title 2");
        doc.appendElement("body").appendElement("p").text("Test Paragraph 2");

        doc.normalise();

        Element head = doc.head();
        Element body = doc.body();

        assertEquals("Test Title", head.getElementsByTag("title").first().text().trim());
        assertEquals("Test Paragraph", body.getElementsByTag("p").first().text().trim());
        assertEquals(0, head.childNodeSize());
        assertEquals(1, body.childNodeSize());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        Element body = doc.appendElement("body");

        Element foundHtml = doc.findFirstElementByTagName("html", doc);
        assertEquals(html, foundHtml);

        Element foundTitle = doc.findFirstElementByTagName("title", head);
        assertEquals(title, foundTitle);

        Element notFound = doc.findFirstElementByTagName("div", doc);
        assertNull(notFound);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        Element body = doc.appendElement("body");
        body.appendElement("p").text("Test Paragraph");

        String htmlString = doc.outerHtml();

        String expectedHtmlString = "<html><head><title></title></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expectedHtmlString, htmlString);
    }

    @Test
    public void testTextDocument() {
        Document doc = new Document("http://example.com");
        doc.body().text("Test Paragraph");

        String text = doc.text();
        assertEquals("Test Paragraph\n", text);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.appendText("Test Title ");
        title.appendText("Test Title 2");
        Element body = html.appendElement("body");
        TextNode textNode = body.appendText(" Test Paragraph ");

        doc.normalise();

        Element normalisedTitle = head.getElementsByTag("title").first();
        assertEquals("Test Title Test Title 2", normalisedTitle.text().trim());
        assertEquals(" Test Paragraph ", textNode.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        OutputSettings escapedOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, escapedOutputSettings.escapeMode());

        String html = doc.outerHtml();
        String expectedHtml = "<!doctype html><html><head></head><body></body></html>";
        assertEquals(expectedHtml, html);

        String htmlEscaped = doc.outerHtml(escapedOutputSettings);
        String expectedHtmlEscaped = "<!doctype html><html><head></head><body></body></html>";
        assertEquals(expectedHtmlEscaped, htmlEscaped);

        assertNotEquals(expectedHtml, htmlEscaped);
    }

    @Test
    public void testOutputSettingsCharset() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        OutputSettings charsetOutputSettings = outputSettings.charset("UTF-16LE");
        assertEquals("UTF-16LE", charsetOutputSettings.charset().name());

        String html = doc.outerHtml();
        String expectedHtml = "<!doctype html><html><head></head><body></body></html>";
        String expectedHtmlWithCharset = "<!doctype html><html xmlns:xml=\'http://www.w3.org/XML/1998/namespace\' xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" + expectedHtml + "</html>";
        assertEquals(expectedHtmlWithCharset, doc.outerHtml(charsetOutputSettings));
    }
}