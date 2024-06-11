package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNull(head);
        document.head().appendElement("title").text("Test Title");
        head = document.head();
        assertNotNull(head);
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNull(body);
        document.body().appendElement("p").text("Test Body");
        body = document.body();
        assertNotNull(body);
        assertEquals("Test Body", body.text());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleSetterNullArgument() {
        Document document = new Document("baseUri");
        try {
            document.title(null);
            fail("Expected Validate.IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.appendElement("html").appendElement("head").appendElement("title").appendText("Test Title");
        document.appendElement("body").appendText("Test Body");
        document.appendText("Test Text");
        document.normalise();
        List<Element> elements = document.select("body > *");
        assertEquals(2, elements.size());
        assertEquals("Test Title", document.title());
        assertEquals("Test BodyTest Text", document.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseMethodWithNullDocument() {
        normalise(null);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        document.appendElement("html").appendElement("head").appendElement("title").appendText("Test Title");
        document.appendElement("body").appendText("Test Body");
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.body().appendText("Test Body");
        assertEquals("Test Body", document.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethodWithNullDocument() {
        Document document = null;
        document.text("Test Text");
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }
}