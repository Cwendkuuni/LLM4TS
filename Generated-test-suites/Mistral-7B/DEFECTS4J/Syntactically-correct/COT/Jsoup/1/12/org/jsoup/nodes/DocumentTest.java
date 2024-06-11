package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHeadAccess() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc = Document.createShell("http://example.com");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyAccess() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc = Document.createShell("http://example.com");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleAccess() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());

        doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleSet() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
        assertNull(doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.text("Test <b>Text</b>");

        doc.normalise();

        Element body = doc.body();
        List<Node> bodyChildren = new ArrayList<>(body.childNodes());
        assertEquals(3, bodyChildren.size());
        Node bodyText = bodyChildren.get(0);
        assertTrue(bodyText instanceof TextNode);
        assertEquals("Test ", bodyText.text());

        Element b = bodyChildren.get(1);
        assertTrue(b instanceof Element);
        assertEquals("b", b.tagName());

        Element textNode = bodyChildren.get(2);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Text", textNode.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document doc = new Document("http://example.com");
        doc.normalise(null);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.text("Test <b>Text</b>");
        doc.normalise();

        assertEquals("<html><head></head><body><text></text><b>Text</b></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test <b>Text</b>");
        doc.normalise();

        assertEquals("Test Text", doc.text().toString());
    }
}