package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testBody() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element p = doc.createElement("p");
        assertEquals("p", p.nodeName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.text("Test Content");
        doc.head().appendElement("title").text("Another Title");
        doc.appendElement("p").text("More Test Content");

        doc.normalise();

        Element body = doc.body();
        assertEquals("Test ContentAnother TitleMore Test Content", body.text());
        assertEquals("Test Title", doc.title());
        assertEquals(1, doc.head().childElementCount());
        assertEquals(0, doc.head().child(0).childElementCount());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", doc);
        assertSame(head.firstChild(), title);

        Element p = doc.findFirstElementByTagName("p", doc);
        assertNull(p);

        p = doc.findFirstElementByTagName("p", body);
        assertSame(body.firstChild(), p);
    }
}