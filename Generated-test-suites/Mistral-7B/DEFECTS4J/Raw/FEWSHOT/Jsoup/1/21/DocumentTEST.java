package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testGetHeadElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();

        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();

        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");

        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);

        doc.title(null);
        assertNull(doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);

        Element link = doc.createElement("a");

        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, link.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.appendElement("html").appendElement("head").appendText("Hello");
        doc.text("World");

        doc.normalise();

        List<Node> headChildren = doc.getElementsByTag("head").first().childNodes();
        List<Node> bodyChildren = doc.getElementsByTag("body").first().childNodes();

        assertTrue(headChildren.isEmpty());
        assertEquals("HelloWorld", bodyChildren.get(0).toString());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.appendElement("html").appendElement("head").appendText("Hello").appendElement("body").appendText("World");

        assertEquals("<html><head></head><body>World</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.appendElement("html").appendElement("head").appendText("Hello").appendElement("body").appendText("World");

        doc.text("Test Text");

        assertEquals("Test Text", doc.text().toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title(null);
    }
}