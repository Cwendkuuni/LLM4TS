package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("", doc.title()); // default title is empty string
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
        assertEquals("", doc.title()); // clearing title sets it to empty string
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element e = doc.createElement("p");
        assertNotNull(e);
        assertEquals("p", e.nodeName());
    }

    @Test
    public void testNormalizeDocument() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = htmlEl.appendElement("head");
        Element bodyEl = htmlEl.appendElement("body");
        bodyEl.appendText("Test ");
        bodyEl.appendText("Text");
        Element pEl = doc.createElement("p");
        pEl.appendText("Another test");
        bodyEl.appendChild(pEl);

        doc.normalise();

        List<Node> textNodes = new ArrayList<>();
        for (Node node : bodyEl.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        assertEquals(3, textNodes.size());
        assertEquals("Test ", textNodes.get(0).text());
        assertEquals("Text", textNodes.get(1).text());
        assertEquals("Another test", textNodes.get(2).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = htmlEl.appendElement("head");
        Element bodyEl = htmlEl.appendElement("body");

        Element titleEl = doc.findFirstElementByTagName("title", bodyEl);
        assertNull(titleEl);

        titleEl = doc.findFirstElementByTagName("title", headEl);
        assertNotNull(titleEl);
        assertEquals("title", titleEl.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = htmlEl.appendElement("head");
        Element bodyEl = htmlEl.appendElement("body");
        bodyEl.appendText("Test ");
        bodyEl.appendText("Text");

        assertEquals("<html><head></head><body>Test Text</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element bodyEl = doc.appendElement("body");
        bodyEl.text("Test Text");

        assertEquals("Test Text", bodyEl.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCloneDocument() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }
}