package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.jsoup.helper.W3C.html;
import static org.jsoup.helper.W3C.head;
import static org.jsoup.helper.W3C.body;
import static org.jsoup.helper.W3C.title;

/**
 * Test class for Document.
 *
 * @author John Doe, john.doe@example.com
 */
public class DocumentTest {

    @org.junit.Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertTrue(doc.body() == null);
        assertTrue(doc.head() == null);
        assertTrue(doc.title().isEmpty());
    }

    @org.junit.Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.body());
        assertNotNull(doc.head());
        assertTrue(doc.title().isEmpty());
        assertEquals(3, doc.childNodes().size());
    }

    @org.junit.Test
    public void testSetAndGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.nodeName());
    }

    @org.junit.Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement(html());
        Element head = html.appendElement(head());
        Element title = head.appendElement(title().text("Test Title"));
        Element body = html.appendElement(body());
        body.appendText("Test text 1 ");
        body.appendText("Test text 2 ");

        doc.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(3, bodyChildren.size());
        TextNode textNode1 = (TextNode) bodyChildren.get(0);
        TextNode textNode2 = (TextNode) bodyChildren.get(1);
        assertEquals("Test text 1 ", textNode1.text());
        assertEquals("Test text 2 ", textNode2.text());
        assertTrue(title.text().equals("Test Title"));
    }

    @org.junit.Test
    public void testAccessors() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(html(), doc.head().parentTag());
        assertEquals(doc, doc.head().parent());
        assertEquals(doc.body(), doc.body().parent());
        assertEquals(doc, doc.body().parentTag());
        assertEquals(doc, doc.title().parent());
        assertEquals(head(), doc.title().parentTag());
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement(html());
        Element head = html.appendElement(head());
        Element title = head.appendElement(title().text("Test Title"));
        Element body = html.appendElement(body());
        body.appendText("Test text 1 ");
        body.appendText("Test text 2 ");

        doc.normalise();

        assertEquals("<html><head><title>Test Title</title></head><body>Test text 1 Test text 2 </body></html>", doc.outerHtml());
    }

    @org.junit.Test
    public void testTextMethod() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement(html());
        Element head = html.appendElement(head());
        Element body = html.appendElement(body());
        body.appendText("Test text 1 ");
        body.appendText("Test text 2 ");

        doc.normalise();

        doc.text("Test text replaced");

        assertEquals("Test text replaced", doc.body().text());
    }
}