package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.Jsoup.parse;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = parse("<html><head></head><body></body></html>");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = parse("<html><head></head><body></body></html>");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitleElementAndText() {
        Document doc = parse("<html><head><title>Example Title</title></head><body></body></html>");
        String title = doc.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitleElementAndText() {
        Document doc = new Document("http://example.com");
        doc.title("New Title");
        assertEquals("New Title", doc.title());

        doc = parse("<html><head></head><body></body></html>");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element link = doc.createElement("a");
        assertEquals("a", link.nodeName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = parse("<html><head><title>Example Title</title></head><body></body></html>");
        doc.normalise();
        List<Node> bodyChildren = doc.body().childNodes();
        assertTrue(bodyChildren.size() > 0);
        for (Node node : bodyChildren) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                assertTrue(textNode.text().trim().length() > 0);
            }
        }
    }

    @Test
    public void testOuterHtml() {
        Document doc = parse("<html><head><title>Example Title</title></head><body></body></html>");
        assertEquals("<html><head><title>Example Title</title></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = parse("<html><head></head><body>Text content</body></html>");
        doc.text("New text content");
        assertEquals("New text content", doc.text().trim());
    }
}