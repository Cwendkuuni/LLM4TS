package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(Tag.HTML, doc.tagName());
        Element html = doc.head();
        assertNotNull(html);
        assertEquals(Tag.HEAD, html.tagName());
        Element head = html.body();
        assertNull(head);
        head = html.appendElement(Tag.BODY);
        assertNotNull(head);
        assertEquals(Tag.BODY, head.tagName());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        // Test normalising empty document
        doc.normalise();
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());

        // Test normalising with text nodes
        Element root = doc.appendElement("html");
        Element head = root.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = root.appendElement("body");
        body.text("Test Body");
        body.appendElement("p").text("Test Paragraph");

        doc.normalise();

        assertEquals("#document", doc.nodeName());
        assertEquals("Test Title", doc.title());
        assertEquals(Tag.HTML, doc.head().tagName());
        assertEquals(Tag.HEAD, doc.head().getFirstElementByTagName("title").tagName());
        assertEquals(Tag.BODY, doc.body().tagName());
        assertEquals("Test Body", doc.body().text());
        assertEquals(Tag.DIV, doc.body().getFirstElementByTagName("p").tagName());
        assertEquals("Test Paragraph", doc.body().getFirstElementByTagName("p").text());
    }

    @Test
    public void testNormaliseTextNodes() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element root = doc.appendElement("html");
        Element head = root.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = root.appendElement("body");
        body.appendElement("p").text("Test Paragraph 1");
        body.appendElement("p").text("Test Paragraph 2");
        body.appendElement("script").appendText("console.log('Hello World')");

        doc.normalise();

        Element newBody = doc.body();
        assertNotNull(newBody);
        List<Node> textNodes = new ArrayList<>();
        for (Node node : newBody.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        assertEquals(3, textNodes.size());
        assertEquals("Test Paragraph 1Test Paragraph 2", textNodes.get(0).text());
        assertEquals("", textNodes.get(1).text());
        assertEquals("", textNodes.get(2).text());
    }

    @Test
    public void testNormaliseStructure() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element root = doc.appendElement("html");
        Element head1 = root.appendElement("head");
        Element body1 = root.appendElement("body");
        Element head2 = root.appendElement("head");
        Element body2 = root.appendElement("body");

        body1.appendElement("p").text("Test Paragraph 1");
        head1.appendElement("title").text("Test Title 1");

        doc.normalise();

        Element masterHead = doc.getElementsByTag("head").first();
        assertEquals(Tag.HEAD, masterHead.tagName());
        assertSame(head1, masterHead.getFirstElementSibling());
        assertSame(body1, masterHead.nextSibling());
        assertNull(masterHead.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        doc.title("Test Title");
        Element head = doc.head();
        head.appendElement("meta").attr("charset", "UTF-8");
        Element body = doc.body();
        body.appendElement("p").text("Test Paragraph");

        doc.normalise();

        String html = doc.outerHtml();
        String expected = "<html><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expected, html);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element body = doc.body();
        doc.text("Test Text", body);
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        doc.title("Test Title");
        Element head = doc.head();
        head.appendElement("meta").attr("charset", "UTF-8");
        Element body = doc.body();
        body.appendElement("p").text("Test Paragraph");

        doc.normalise();

        Document cloned = doc.clone();
        String html1 = doc.outerHtml();
        String html2 = cloned.outerHtml();
        assertEquals(html1, html2);
    }

    @Test
    public void testOutputSettings() {
        Charset charset = StandardCharsets.UTF_8;
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.charset(charset);

        Document doc = Document.createShell("http://example.com");
        doc.outputSettings(outputSettings);

        assertEquals(charset, doc.outputSettings().charset());
        assertEquals(charset.name(), doc.outputSettings().charset().name());
    }
}