package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document doc;

    @Before
    public void setup() {
        doc = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body() != null);
        assertTrue(doc.head() != null);
    }

    @Test
    public void testHeadElement() {
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyElement() {
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleElementGet() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleElementSet() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title(null);
        assertNull(doc.title());

        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        doc.appendElement("p").text("Test");
        doc.appendElement("p").text("Test2");
        doc.appendElement("html").text("Test3");

        doc.normalise();

        List<Element> paras = doc.select("p");
        assertEquals(2, paras.size());
        assertEquals("Test", paras.get(0).text());
        assertEquals("Test2", paras.get(1).text());
        assertNull(doc.select("html").first());
    }

    @Test
    public void testOuterHtml() {
        doc.appendElement("p").text("Test");
        doc.text("<html><body><h1>Test Title</h1></body></html>");

        assertEquals("<html><body><p>Test</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        doc.appendElement("p").text("Test");
        doc.text("Test Text");

        assertEquals("Test Text", doc.text());
    }
}