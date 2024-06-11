package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://test.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://test.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://test.com");
        doc.title("Test Title");
        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://test.com");
        Element body = doc.body();
        assertTrue(body.isEmpty());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://test.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://test.com");
        doc.title("Test Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://test.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals("http://test.com", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://test.com");
        doc.body().appendText("Test ");
        doc.appendText("Text");
        doc.title("Test Title");

        doc.normalise();

        Element body = doc.body();
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Test Text", textNode.text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://test.com");
        doc.head().appendText("Test Head Text");
        doc.body().appendText("Test Body Text 1");
        doc.appendText("Test Body Text 2");

        doc.normalise();

        Element body = doc.body();
        TextNode textNode1 = (TextNode) body.child(0);
        TextNode textNode2 = (TextNode) body.child(1);
        assertEquals("Test Body Text 1Test Body Text 2", textNode1.text());
        assertEquals("Test Body Text 2", textNode2.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://test.com");
        Element body1 = doc.createElement("body");
        doc.appendChild(body1);
        Element body2 = doc.createElement("body");
        doc.appendChild(body2);

        body1.appendText("Test 1");
        body2.appendText("Test 2");

        doc.normalise();

        Element body = doc.body();
        assertEquals(1, body.childSize());
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Test 1Test 2", textNode.text());
    }
}