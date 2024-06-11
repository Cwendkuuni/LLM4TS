package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testAccessHeadAndBody() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("#document", p.parentNode().nodeName());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.body().appendText("Test ");
        doc.head().appendElement(doc.createElement("p")).text("Test");
        doc.normalise();
        Element body = doc.body();
        TextNode textNode = (TextNode) body.getElementsByTag("p").first().child(0);
        assertEquals("Test ", textNode.text());
    }

    @Test
    public void testNormaliseStructure() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.appendElement("html").appendElement("head").appendElement("body") // root = html
                .appendElement("html").appendElement("head").appendElement("body"); // dupes = head, body
        doc.normalise();
        Element root = doc.getElementsByTag("html").first();
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.appendElement("html").appendElement("head").appendElement("body") // root = html
                .appendElement("html").appendElement("head").appendElement("body"); // dupes = head, body
        Element head = doc.findFirstElementByTagName("head", doc.head());
        assertNotNull(head);
        Element body = doc.findFirstElementByTagName("body", doc);
        assertNotNull(body);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.findFirstElementByTagName("body", null);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        doc.body().appendChild(p);
        p.text("Test");
        String html = doc.outerHtml();
        assertTrue(html.contains("<p>Test</p>"));
    }

    @Test
    public void testText() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        doc.body().appendChild(p);
        p.text("Test");
        String text = doc.text();
        assertTrue(text.contains("Test"));
    }

    @Test
    public void testNodeName() {
        String baseUri = "https://example.com";        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().name());
        assertEquals(true, settings.prettyPrint());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().charset("UTF-16");
        assertEquals("UTF-16", doc.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().indentAmount(16);
        assertEquals(16, doc.outputSettings().indentAmount());
    }
}