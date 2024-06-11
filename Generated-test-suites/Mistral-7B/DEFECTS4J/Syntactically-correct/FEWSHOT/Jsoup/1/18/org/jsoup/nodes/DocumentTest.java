package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
    public void testCreateNewDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertTrue(doc.body() == null);
        assertTrue(doc.head() == null);
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
        Element html = doc.select("html").first();
        assertEquals("#html", html.nodeName());
        Element head = html.getElementsByTag("head").first();
        assertEquals("#head", head.nodeName());
        Element body = html.getElementsByTag("body").first();
        assertEquals("#body", body.nodeName());
        assertTrue(doc.title() == null);
    }

    @Test
    public void testGetDocumentTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetDocumentTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElementWithDocumentBaseUri() {
        Document doc = new Document("baseUri");
        Element link = doc.createElement("a");
        assertEquals("a", link.nodeName());
        assertEquals("baseUri", link.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title").text("Test Title");
        Element p = doc.createElement("p").text("Test text");
        body.appendChild(title);
        body.appendChild(p);
        body.appendChild(doc.createTextNode(" "));

        doc.normalise();
        Element newTitle = doc.getElementsByTag("title").first();
        assertEquals("Test Title", newTitle.text());
        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        assertEquals("Test text", bodyChildren.get(0).text());
        assertEquals("", bodyChildren.get(1).text());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("baseUri");
        doc.normalise();
        List<Node> documentChildren = doc.childNodes();
        assertEquals(3, documentChildren.size());
        assertEquals("#html", documentChildren.get(0).nodeName());
        assertEquals("#head", documentChildren.get(1).nodeName());
        assertEquals("#body", documentChildren.get(2).nodeName());
    }

    @Test
    public void testNormaliseDocumentWithEmptyTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        doc.normalise();
        Element title = doc.getElementsByTag("title").first();
        assertNull(title);
    }

    @Test
    public void testNormaliseDocumentWithEmptyBody() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.normalise();
        Element body = doc.body();
        assertSame(body, doc.getElementsByTag("body").first());
        assertTrue(body.childNodes().isEmpty());
    }

    @Test
    public void testNormaliseDocumentWithEmptyTitleAndBody() {
        Document doc = new Document("baseUri");
        doc.normalise();
        Element title = doc.getElementsByTag("title").first();
        assertNull(title);
        Element body = doc.body();
        assertTrue(body.childNodes().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseDocumentWithNullDocument() {
        Document doc = null;
        doc.normalise();
    }

    @Test
    public void testNormaliseDocumentWithNullElement() {
        Document doc = new Document("baseUri");
        doc.normalise();
        Document doc2 = new Document("baseUri2");
        doc.normalise(doc2);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title").text("Test Title");
        Element p = doc.createElement("p").text("Test text");
        body.appendChild(title);
        body.appendChild(p);
        body.appendChild(doc.createTextNode(" "));

        doc.normalise();
        String outerHtml = doc.outerHtml();
        String expected = "<html><head></head><body><title>Test Title</title><p>Test text</p></body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.body().text("Test text");
        String text = doc.text();
        assertEquals("Test text", text);
    }

    @Test
    public void testTextWithEmptyDocument() {
        Document doc = new Document("baseUri");
        String text = doc.text();
        assertEquals("", text);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }
}