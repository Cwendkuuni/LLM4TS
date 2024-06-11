package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#root", doc.tagName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
        assertEquals(0, doc.childElementCount());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().tagName());
        assertEquals("head", doc.head().firstChild().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals(3, doc.childElementCount());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
        assertSame(doc, head.parent());
        assertSame(doc, body.parent());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title(null);
        assertNull(doc.title());

        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.body().appendText("Test Text");

        Element h1 = doc.createElement("h1").text("Test Heading");
        doc.head().append(h1);

        doc.normalise();

        assertEquals("html<head><title>Test Title</title><body>Test Text</body></html>", doc.outerHtml());
    }

    @Test
    public void testNormaliseNullElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.normalise(null);
        assertEquals("html<head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testNormaliseTextNodes() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element p = doc.createElement("p");
        p.appendChild(new TextNode("Test Text 1"));
        p.appendChild(new TextNode("Test Text 2"));
        doc.body().appendChild(p);

        doc.normalise();

        assertEquals("html<head><body>Test Text 1 Test Text 2</body></html>", doc.outerHtml());
    }
}