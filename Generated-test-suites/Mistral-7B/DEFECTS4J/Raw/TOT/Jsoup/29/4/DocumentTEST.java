package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("head", html.nodeName());
        Element head = html.appendElement("head");
        Element body = doc.body();
        assertNull(body);
        Element title = doc.title();
        assertNull(title);
    }

    @Test
    public void testGetHeadAndBodyElements() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        String title = doc.title();
        assertEquals("Test", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p> Text 1</p><p> Text 2</p><p><p>Text 3</p></p></body></html>", "http://example.com");
        doc.normalise();
        Element body = doc.body();
        List<Element> pElements = body.select("p");
        assertEquals(3, pElements.size());
        for (Element pElement : pElements) {
            assertEquals("p", pElement.nodeName());
            assertEquals(pElement.text(), pElement.outerHtml().substring(pElement.text().length()));
        }
    }
}