package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testConstructorWithBaseUri() {
        Document document = new Document("baseUri");
        assertNotNull(document);
    }

    @Test
    public void testCreateShellWithBaseUri() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHeadGetter() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyGetter() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleGetter() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetterWithNullTitle() {
        Document document = new Document("baseUri");
        try {
            document.title(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("title cannot be null", e.getMessage());
        }
    }

    @Test
    public void testTitleSetter() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementWithTagName() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.tagName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("baseUri");
        document.text("Text before normalise");
        document.appendElement("html").text("Text inside html");
        document.appendElement("head").text("Text inside head");
        document.appendElement("body").text("Text inside body");

        document.normalise();

        TextNode textNode = (TextNode) document.body().get(0);
        assertEquals("Text before normaliseText inside body", textNode.text());
        TextNode textNodeInsideHtml = (TextNode) document.select("html").first().get(0);
        assertEquals("Text inside html", textNodeInsideHtml.text());
        TextNode textNodeInsideHead = (TextNode) document.head().get(0);
        assertEquals("", textNodeInsideHead.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document document = new Document("baseUri");
        document.normalise(null);
    }

    @Test
    public void testOuterHtmlGetter() {
        Document document = new Document("baseUri");
        document.text("Test Document");
        assertEquals("Test Document", document.outerHtml());
    }

    @Test
    public void testTextSetterWithNullText() {
        Document document = new Document("baseUri");
        try {
            document.text(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("text cannot be null", e.getMessage());
        }
    }

    @Test
    public void testTextSetter() {
        Document document = new Document("baseUri");
        document.text("Test Document");
        assertEquals("Test Document", document.text().get(0).text());
    }

    @Test
    public void testNodeNameGetter() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }
}