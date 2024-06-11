package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java.
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
        assertNull(doc.titleElement());
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
        assertNull(doc.titleElement());

        Element html = doc.head();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
    }

    /**
     * Test head and body accessors.
     */
    @Test
    public void testHeadAndBodyAccessors() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(head);
        assertNotNull(body);

        // Test getElementById
        Element title = doc.createElement("title").id("title1");
        head.appendChild(title);
        Element title1 = doc.getElementById("title1");
        assertNotNull(title1);
        assertEquals("title", title1.tagName());

        // Test title method
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", title1.text());

        // Test titleElement method
        Element titleElement = doc.titleElement();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());

        // Test element methods on empty document
        assertNull(doc.head().previousSibling());
        assertNull(doc.head().nextSibling());
        assertNull(doc.body().previousSibling());
        assertNull(doc.body().nextSibling());

        // Test element methods on populated document
        Element p1 = doc.createElement("p").text("Text 1");
        body.appendChild(p1);
        Element p2 = doc.createElement("p").text("Text 2");
        body.appendChild(p2);
        Element p3 = doc.createElement("p").text("Text 3");
        body.insertBefore(p3, p1);

        Element p1Prev = p1.previousSibling();
        assertNull(p1Prev);
        Element p1Next = p1.nextSibling();
        assertEquals(p2, p1Next);
        Element p3Prev = p3.previousSibling();
        assertEquals(p1, p3Prev);
        Element p3Next = p3.nextSibling();
        assertEquals(p2, p3Next);
    }

    /**
     * Test text method.
     */
    @Test
    public void testTextMethod() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();

        // Test empty document
        doc.text("Test Text");
        assertEquals("Test Text", doc.outerHtml());

        // Test populated document
        Element p1 = doc.createElement("p").text("Text 1");
        body.appendChild(p1);
        Element p2 = doc.createElement("p").text("Text 2");
        body.appendChild(p2);

        doc.text("New Text");

        assertEquals("New Text" + System.lineSeparator() + p1.outerHtml() + System.lineSeparator() + p2.outerHtml(), doc.outerHtml());
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormaliseMethod() {
        Document doc = Document.createShell("http://example.com");

        // Test normalise with empty document
        doc.normalise();
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());

        // Test normalise with text nodes outside body
        Element p1 = doc.createElement("p").text("Text 1");
        doc.appendChild(p1);
        doc.normalise();
        assertNull(p1.parent());
        assertEquals(doc.body(), p1.parent());

        // Test normalise with text nodes inside head
        Element title = doc.createElement("title").text("Title 1");
        doc.head().appendChild(title);
        Element script = doc.createElement("script").text("Script 1");
        doc.head().appendChild(script);
        doc.normalise();
        assertEquals(title, doc.head().firstChild());
        assertEquals(script, title.nextSibling());

        // Test normalise with text nodes inside body
        Element p2 = doc.createElement("p").text("Text 2");
        doc.body().appendChild(p2);
        Element textNode1 = doc.createElement("textNode1").text("Text Node 1");
        doc.body().insertBefore(textNode1, p2);
        Element textNode2 = doc.createElement("textNode2").text("Text Node 2");
        doc.body().appendChild(textNode2);
        doc.normalise();
        assertEquals(textNode1, p2.previousSibling());
        assertEquals(p2, textNode1.nextSibling());
        assertEquals(textNode2, p2.nextSibling());
    }
}