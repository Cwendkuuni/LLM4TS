package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Set up any state that is needed before each test
    }

    @org.junit.After
    public void tearDown() {
        // Clean up any state that is no longer needed
    }

    /**
     * Test constructor Document.
     */
    @org.junit.Test
    public void testDocument() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals("baseUri", document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    /**
     * Test method createShell.
     */
    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals("baseUri", document.baseUri());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.firstChild();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = head.nextSibling();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertNull(document.title());
    }

    /**
     * Test method head.
     */
    @org.junit.Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals("Test Title", head.child(0).text());
    }

    /**
     * Test method body.
     */
    @org.junit.Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNull(body);
        document = Document.createShell("baseUri");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        document.title("Test Title");
        TextNode title = (TextNode) document.head().child(0);
        TextNode textNode = document.createElement("p").text("Test Text");
        body.appendChild(textNode);
        Element p = document.body().getElementsByTag("p").first();
        assertEquals("Test Text", p.text());
        assertEquals("Test Title", document.title());
    }

    /**
     * Test method title.
     */
    @org.junit.Test
    public void testTitle() {
        Document document = new Document("baseUri");
        assertNull(document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        document.title(null);
        assertNull(document.title());
    }

    /**
     * Test method createElement.
     */
    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element p = document.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals("baseUri", p.baseUri());
    }

    /**
     * Test method normalise.
     */
    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.body().appendChild(document.createElement("p").text("Test Text"));
        document.body().appendChild(document.createElement("p").text("Test Text 2"));
        document.head().appendChild(document.createElement("p").text("Test Title"));
        document.head().appendChild(document.createElement("p").text("Test Title 2"));
        document.normalise();
        Element body = document.body();
        Element head = document.head();
        assertEquals(1, body.childNodes().size());
        assertEquals("Test Text", body.childNodes().get(0).text());
        assertEquals(0, head.childNodes().size());
    }
}