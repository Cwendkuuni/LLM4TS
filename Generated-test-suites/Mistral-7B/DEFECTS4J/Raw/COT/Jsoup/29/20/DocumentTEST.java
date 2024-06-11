package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentCreate() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testDocumentCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNull(html);
        html = doc.body();
        assertNull(html);
    }

    @Test
    public void testDocumentHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        head = doc.head();
        Element title = head.getElementsByTag("title").first();
        assertNull(title);
        doc.title("Test Title");
        title = doc.head().getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testDocumentBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
        body = doc.body();
        assertTrue(body.nodeName().equals("body"));
    }

    @Test
    public void testDocumentTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testDocumentCreateElement() {
        Document doc = new Document("http://example.com");
        Element div = doc.createElement("div");
        assertEquals("div", div.nodeName());
        assertEquals("http://example.com", div.baseUri());
    }

    @Test
    public void testDocumentNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        TextNode text1 = doc.appendTextNode("Text 1");
        TextNode text2 = doc.appendTextNode("Text 2");
        TextNode text3 = doc.appendTextNode("Text 3");
        TextNode text4 = doc.appendTextNode("Text 4");

        doc.normalise();

        Element htmlEl = findFirstElementByTagName("html", doc);
        assertEquals(htmlEl, html);
        Element headEl = findFirstElementByTagName("head", htmlEl);
        assertEquals(headEl, head);
        Element bodyEl = findFirstElementByTagName("body", htmlEl);
        assertEquals(bodyEl, body);
        Node text1El = findFirstTextNode(doc);
        assertEquals(text1El, text1);
        Node text2El = findFirstTextNode(doc);
        assertEquals(text2El, text2);
        Node text3El = findFirstTextNode(doc);
        assertEquals(text3El, text3);
        Node text4El = findFirstTextNode(doc);
        assertEquals(text4El, text4);
    }

    private Element findFirstElementByTagName(String tag, Node node) {
        if (node.nodeName().equals(tag))
            return (Element) node;
        else {
            for (Node child: node.childNodes) {
                Element found = findFirstElementByTagName(tag, child);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    private TextNode findFirstTextNode(Document doc) {
        for (Node node : doc.childNodes()) {
            if (node instanceof TextNode) {
                return (TextNode) node;
            }
        }
        return null;
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentNormaliseWithNullElement() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        Node node = null;
        doc.normaliseTextNodes(node);
    }

    @Test
    public void testDocumentNormaliseTextNodesWithEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normaliseTextNodes(doc);
        List<Node> nodes = new ArrayList<>();
        for (Node node : doc.childNodes()) {
            nodes.add(node);
        }
        assertEquals(nodes.size(), 0);
    }

    @Test
    public void testDocumentNormaliseTextNodesWithSingleTextNode() {
        Document doc = new Document("http://example.com");
        TextNode textNode = doc.appendTextNode("Test Text");
        doc.normaliseTextNodes(doc);
        List<Node> nodes = new ArrayList<>();
        for (Node node : doc.childNodes()) {
            nodes.add(node);
        }
        assertEquals(nodes.size(), 3);
        assertEquals(nodes.get(0).nodeName(), "#document");
        Element htmlEl = (Element) nodes.get(1);
        assertEquals(htmlEl.nodeName(), "html");
        Element bodyEl = htmlEl.getFirstElementChild();
        assertEquals(bodyEl.nodeName(), "body");
        TextNode textEl = (TextNode) nodes.get(2);
        assertEquals("Test Text", textEl.text());
    }

    @Test
    public void testDocumentNormaliseTextNodesWithMultipleTextNodes() {
        Document doc = new Document("http://example.com");
        TextNode textNode1 = doc.appendTextNode("Test Text 1");
        TextNode textNode2 = doc.appendTextNode("Test Text 2");
        TextNode textNode3 = doc.appendTextNode("Test Text 3");
        TextNode textNode4 = doc.appendTextNode("Test Text 4");
        doc.normaliseTextNodes(doc);
        List<Node> nodes = new ArrayList<>();
        for (Node node : doc.childNodes()) {
            nodes.add(node);
        }
        assertEquals(nodes.size(), 3);
        assertEquals(nodes.get(0).nodeName(), "#document");
        Element htmlEl = (Element) nodes.get(1);
        assertEquals(htmlEl.nodeName(), "html");
        Element bodyEl = htmlEl.getFirstElementChild();
        assertEquals(bodyEl.nodeName(), "body");
        TextNode textEl1 = (TextNode) nodes.get(2);
        assertEquals("Test Text 1Test Text 2", textEl1.text());
        TextNode textEl2 = (TextNode) nodes.get(nodes.size() - 1);
        assertEquals("Test Text 3Test Text 4", textEl2.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentNormaliseTextNodesWithNonTextNode() {
        Document doc = new Document("http://example.com");
        TextNode textNode = doc.appendTextNode("Test Text");
        doc.normaliseTextNodes(doc.createElement("div"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentNormaliseTextNodesWithNullDocument() {
        Document doc = new Document("http://example.com");
        doc.normaliseTextNodes(null);
    }

    @Test
    public void testDocumentOuterHtml() {
        Document doc = new Document("http://example.com");
        TextNode textNode = doc.appendTextNode("Test Text");
        assertEquals("", doc.outerHtml());
        doc.title("Test Title");
        doc.text("Test Text");
        doc.appendElement("div").text("Test Div");
        assertEquals("<html><head><title>Test Title</title></head><body>Test Text<div>Test Div</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testDocumentText() {
        Document doc = new Document("http://example.com");
        TextNode textNode = doc.appendTextNode("Test Text");
        assertEquals("Test Text", doc.text());
        doc.title("Test Title");
        doc.text("Test Text 1Test Text 2");
        doc.appendElement("div").text("Test Div");
        assertEquals("Test Text 1Test Text 2Test Div", doc.text());
    }

    @Test
    public void testDocumentNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testDocumentClone() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        Document clone = doc.clone();
        OutputSettings cloneOutputSettings = clone.outputSettings();
        assertEquals(outputSettings, cloneOutputSettings);
    }

    @Test
    public void testDocumentOutputSettingsEscapeMode() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        doc.outputSettings(new OutputSettings().escapeMode(Entities.EscapeMode.extended));
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
    }

    @Test
    public void testDocumentOutputSettingsCharset() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        doc.outputSettings(new OutputSettings().charset(Charset.forName("ISO-8859-1")));
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
    }

    @Test
    public void testDocumentOutputSettingsPrettyPrint() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertTrue(outputSettings.prettyPrint());
        doc.outputSettings(new OutputSettings().prettyPrint(false));
        assertFalse(doc.outputSettings().prettyPrint());
    }
}