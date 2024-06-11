package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("#root", doc.nodeName());
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.title());
        assertTrue(doc.head() == null);
        assertTrue(doc.body() == null);
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals(3, doc.childNodes().size());
        assertEquals("#root", doc.nodeName());
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.title());
        assertEquals("", doc.title());
        assertNotNull(doc.head());
        assertEquals(1, doc.head().childNodes().size());
        assertEquals("#head", doc.head().nodeName());
        assertEquals(Tag.valueOf("head"), doc.head().tagName());
        assertNotNull(doc.body());
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        Element body = doc.body();

        assertTrue(head.parentNode().equals(doc));
        assertTrue(body.parentNode().equals(doc));
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName().toString());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        Element p = doc.createElement("p");
        p.text("Test text");
        doc.body().appendChild(p);

        doc.normalise();

        Element body = doc.body();
        List<Node> children = body.childNodes();
        assertEquals(3, children.size());
        assertEquals("#text", children.get(0).nodeName());
        assertTrue(children.get(0) instanceof TextNode);
        assertEquals("Test text", ((TextNode) children.get(0)).text());
        assertEquals("#p", children.get(1).nodeName());
        assertEquals("p", children.get(1).tagName().toString());
        assertEquals(baseUri, children.get(1).baseUri());
        assertEquals(0, children.get(2).childNodes().size());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        head.appendElement("title").text("Test Title");
        Element body = doc.body();
        body.appendChild(doc.createElement("p").text("Test text"));

        doc.normalise();

        String expected = "<html><head><title>Test Title</title></head><body>" +
                "<p>Test text</p>" +
                "</body></html>";
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        body.text("Test text");

        doc.normalise();

        List<Node> children = body.childNodes();
        assertEquals(1, children.size());
        assertEquals("#text", children.get(0).nodeName());
        assertTrue(children.get(0) instanceof TextNode);
        assertEquals("Test text", ((TextNode) children.get(0)).text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title(null);
    }
}