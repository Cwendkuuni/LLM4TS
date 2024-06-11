package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertEquals("html", doc.tagName());
        Element html = doc.getElementsByTag("html").first();
        assertEquals(1, html.childNodes().size());
        assertSame(html.childNodes().get(0), doc.head());
        assertEquals(2, html.childNodes().size());
        assertEquals("head", html.getElementsByTag("head").get(0).nodeName());
        assertEquals("body", html.getElementsByTag("body").get(0).nodeName());
    }

    @Test
    public void testHeadAccess() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        assertEquals("head", head.nodeName());
        assertEquals(1, head.childNodes().size());
        assertNull(head.text());
    }

    @Test
    public void testBodyAccess() {
        Document doc = Document.createShell("baseUri");
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testTitleAccess() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", doc.head().getElementsByTag("title").get(0).text());
    }

    @Test
    public void testTitleSettingWithExistingElement() {
        Document doc = Document.createShell("baseUri");
        doc.head().appendElement("title").text("Old Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        assertEquals("New Title", doc.head().getElementsByTag("title").get(0).text());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element newElement = doc.createElement("a");
        assertEquals("a", newElement.nodeName());
        assertEquals("baseUri", newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        doc.text("Normalise this document\nfoo bar");
        doc.head().appendElement("title").text("Test Title");
        doc.head().appendElement("div").text("Test Div");
        doc.appendElement("div").text("Test Div 2");

        doc.normalise();

        assertEquals("html\n<head>\n<title>Test Title</title>\n</head>\n<body>\nfoo bar\n</body>", doc.outerHtml());
    }

    @Test
    public void testTextSetting() {
        Document doc = Document.createShell("baseUri");
        doc.text("Test test");
        assertEquals("Test test", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
    }
}