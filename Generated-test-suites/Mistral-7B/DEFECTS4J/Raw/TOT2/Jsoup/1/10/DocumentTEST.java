package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testTitleMethodEmpty() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleMethodWithTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormaliseMethodEmpty() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Test Content");
        assertEquals("<html><head></head><body>Test Content</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextMethodEmpty() {
        Document doc = new Document("http://example.com");
        doc.body().text("Test Content");
        assertEquals("Test Content", doc.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethodNullText() {
        Document doc = new Document("http://example.com");
        doc.text(null);
    }

    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testConstructorWithNullBaseUri() {
        try {
            new Document(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateShellWithNullBaseUri() {
        try {
            Document.createShell(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHeadMethodNullHead() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
    }

    @Test
    public void testBodyMethodNullBody() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
    }

    @Test
    public void testTitleMethodNullTitleEl() {
        Document doc = new Document("http://example.com");
        Element titleEl = doc.getElementsByTag("title").first();
        assertNull(titleEl);
    }

    @Test
    public void testTitleMethodNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElementMethodInvalidTagName() {
        Document doc = new Document("http://example.com");
        try {
            doc.createElement("InvalidTagName");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNormaliseMethodWithValidStructure() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("body").appendText("Test Content");
        doc.normalise();
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Test Content", doc.body().text());
    }

    @Test
    public void testNormaliseMethodWithTextNodesOutsideBody() {
        Document doc = new Document("http://example.com");
        Element root = doc.appendElement("html").appendElement("head").appendElement("textNode1").text("Test1");
        doc.appendElement("body");
        doc.normalise();
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Test1", doc.getTextContentOf("html"));
    }

    @Test
    public void testTextMethodWithEmptyText() {
        Document doc = new Document("http://example.com");
        doc.body().text("");
        assertEquals("", doc.text());
    }

    @Test
    public void testOuterHtmlMethodWithOtherElements() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body").appendElement("div");
        doc.normalise();
        assertEquals("<html><head></head><body><div></div></body></html>", doc.outerHtml());
    }

    
}