package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().childNodes().size() > 0);
        assertTrue(doc.body().childNodes().size() == 0);
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
        assertNotSame(head, body);
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Head");
        doc.appendText("Test Text 1");
        doc.body().appendText("Test Text 2");
        doc.appendText("Test Text 3");

        doc.normalise();

        assertNull(doc.head().childNodes().first());
        assertNull(doc.body().childNodes().first());
        List<Node> bodyTextNodes = doc.body().childNodes();
        assertEquals(3, bodyTextNodes.size());
        for (int i = 0; i < 3; i++) {
            TextNode textNode = (TextNode) bodyTextNodes.get(i);
            assertEquals("Test Text " + (i + 1), textNode.text());
        }
    }
}