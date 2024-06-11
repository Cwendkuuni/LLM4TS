package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("base-uri");
        assertEquals("#root", doc.tagName());
        assertEquals("base-uri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("base-uri");
        assertEquals("#root", doc.tagName());
        assertEquals("base-uri", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("base-uri");
        doc.head(); // no checking as it just returns the head Element
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertEquals("base-uri", head.baseUri());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("base-uri");
        doc.body(); // no checking as it just returns the body Element
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals("base-uri", body.baseUri());
    }

    @Test
    public void testTitleMethodGet() {
        Document doc = new Document("base-uri");
        assertEquals("", doc.title());
        doc.title("title");
        assertEquals("title", doc.title());
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleMethodSet() {
        Document doc = new Document("base-uri");
        doc.title("title");
        assertEquals("title", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementNullTagName() {
        Document doc = new Document("base-uri");
        doc.createElement(null);
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("base-uri");
        doc.appendElement("html").appendElement("head").appendElement("title").text("title");
        doc.appendElement("body").appendElement("text1").text("text1");
        doc.appendElement("html").appendElement("text2").text("text2");

        doc.normalise();
        Element body = doc.body();
        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        assertEquals("title", doc.title());
    }

    @Test
    public void testNormaliseMethodWithEmptyDocument() {
        Document doc = new Document("base-uri");
        doc.normalise();
        assertEquals(3, doc.numberOfChildren());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodSetNullTitle() {
        Document doc = new Document("base-uri");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodSetEmptyTitle() {
        Document doc = new Document("base-uri");
        doc.title("");
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseMethodNullElement() {
        Document doc = new Document("base-uri");
        doc.normalise(null);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("base-uri");
        doc.appendElement("html").appendElement("head").appendElement("title").text("title");
        doc.appendElement("body").appendElement("text1").text("text1");

        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testTextMethodSetNullText() {
        Document doc = new Document("base-uri");
        doc.text(null);
    }

    @Test
    public void testTextMethodSetEmptyText() {
        Document doc = new Document("base-uri");
        doc.text("");
    }

    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("base-uri");
        assertEquals("#document", doc.nodeName());
    }
}