package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());

        Element html = doc.select("html").first();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadAndTitle() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Element title = doc.title();

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", title.text());

        TextNode oldTitleText = title.text();
        doc.title(null);
        assertEquals("", doc.title());
        assertNull(title.text());

        doc.title("Test Title Again");
        assertEquals("Test Title Again", doc.title());
        assertEquals("Test Title Again", title.text());

        doc.title("Test Title Again");
        assertEquals("Test Title Again", doc.title());
        assertEquals("Test Title Again", title.text());

        doc.title(oldTitleText);
        assertEquals("Test Title Again", doc.title());
        assertEquals("Test Title Again", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
        assertEquals("http://example.com", newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendText("Test Text");

        doc.normalise();

        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(2, bodyChildren.size());
        assertSame(doc.title(), bodyChildren.get(0));
        assertEquals(TextNode.class, bodyChildren.get(1).getClass());
        assertEquals("Test Text", ((TextNode) bodyChildren.get(1)).text());
    }

    @Test
    public void testNormaliseNoNodes() {
        Document doc = new Document("http://example.com");
        doc.normalise();

        List<Node> bodyChildren = doc.body().childNodes();
        assertEquals(0, bodyChildren.size());
    }

    @Test
    public void testOuterHtmlAndText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendText("Test Text");

        doc.normalise();

        assertEquals("<html><head><title>Test Title</title></head><body>Test Text</body></html>", doc.outerHtml());
        assertEquals("Test Text", doc.text().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testTitleEmptyString() {
        Document doc = new Document("http://example.com");
        doc.title("");
        assertEquals("", doc.title());
    }
}