package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNotNull(document.head().tagName());
        assertNotNull(document.body().tagName());
        assertEquals("html", document.head().tagName());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testHeadAccessor() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyAccessor() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleAccessor() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        String title = document.title();
        assertNotNull(title);
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetter() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title("New Title");
        String title = document.title();
        assertNotNull(title);
        assertEquals("New Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleSetterNullTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document document = Jsoup.parse("<html><head><text>Text in Head</text></head><body><text>Text in Body</text></body></html>");
        document.normalise();
        List<Node> bodyNodes = document.body().childNodes();
        assertEquals(2, bodyNodes.size());
        TextNode textInBody = (TextNode) bodyNodes.get(1);
        assertEquals("Text in Body", textInBody.text());
    }

    @Test
    public void testNormaliseWithEmptyDocument() {
        Document document = new Document("baseUri");
        document.normalise();
        List<Node> documentNodes = document.childNodes();
        assertEquals(3, documentNodes.size());
        assertEquals("html", documentNodes.get(0).tagName());
        assertEquals("head", documentNodes.get(1).tagName());
        assertEquals("body", documentNodes.get(2).tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testHeadWhenNoHead() {
        Document document = Jsoup.parse("<html><body></body></html>");
        document.head();
    }

    @Test
    public void testBodyWhenNoBody() {
        Document document = Jsoup.parse("<html><head></head></html>");
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitleWhenNoTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        String title = document.title();
        assertEquals("", title);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentWithNullBaseUri() {
        new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellWithNullBaseUri() {
        Document.createShell(null);
    }

}