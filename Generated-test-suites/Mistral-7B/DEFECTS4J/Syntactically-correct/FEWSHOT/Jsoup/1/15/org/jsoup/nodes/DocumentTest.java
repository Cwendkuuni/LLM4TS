package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertTrue(head.parentElement() instanceof Element);
        assertTrue(head.parentElement() == doc.getElementsByTag("html").first());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertTrue(body.parentElement() instanceof Element);
        assertTrue(body.parentElement() == doc.getElementsByTag("html").first());
    }

    @Test
    public void testGetTitleElement() {
        Document doc = Document.createShell("http://example.com");
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test
    public void testSetTitleElement() {
        Document doc = Document.createShell("http://example.com");
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
        Element titleElement = doc.getElementsByTag("title").first();
        assertEquals(title, titleElement.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertTrue(element.baseUri().equals(doc.baseUri()));
    }

    @Test
    public void testNormaliseDocument() {
        String html = "<html><head></head><body><p>Test</p><div>Test2</div></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        List<Element> bodyChildren = doc.getElementsByTag("body").first().children();
        assertEquals(1, bodyChildren.size());
        Element p = bodyChildren.get(0);
        assertEquals("Test", p.text().trim());
    }

    @Test
    public void testOuterHtml() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Document doc = Document.parse(html);
        assertEquals(html, doc.outerHtml());
    }

    @Test
    public void testTextDocument() {
        String text = "Test";
        Document doc = Document.createShell("http://example.com");
        doc.text(text);
        assertEquals(text, doc.body().text().trim());
    }

}