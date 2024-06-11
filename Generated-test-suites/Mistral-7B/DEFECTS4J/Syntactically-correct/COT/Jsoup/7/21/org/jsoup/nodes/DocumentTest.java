package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.tagName());
    }

    @org.junit.Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());
    }

    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.outerHtml("<html><body><head><title>Test</title></head><body><p>Test</p></body></html>");

        document.normalise();

        Element body = document.body();
        assertEquals("<p>Test</p>", body.outerHtml());
        Element title = document.getElementsByTag("title").first();
        assertEquals("Test", title.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document document = new Document(null);
    }

    @org.junit.Test
    public void testGetCharset() {
        Document document = new Document("baseUri");
        Charset charset = document.outputSettings().charset();
        assertEquals("UTF-8", charset.name());
    }

    @org.junit.Test
    public void testSetCharset() {
        Document document = new Document("baseUri");
        Charset charset = Charset.forName("ISO-8859-1");
        document.outputSettings().charset(charset);
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @org.junit.Test
    public void testNormaliseTextNodesEdgeCase() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element body = document.appendElement("body");
        Element head = document.appendElement("head");
        TextNode textNode1 = document.appendText("Text 1");
        TextNode textNode2 = document.appendText("Text 2");
        TextNode textNode3 = document.appendText("Text 3");

        document.normalise();

        assertEquals("Text 1Text 2Text 3", body.text());
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameEdgeCase() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head1 = document.appendElement("head");
        Element body1 = document.appendElement("body");
        Element head2 = document.appendElement("head");
        Element parent = document.appendElement("parent");

        Element head = document.findFirstElementByTagName("head", parent);
        assertEquals("head", head.tagName());

        head = document.findFirstElementByTagName("head", body1);
        assertNull(head);

        head = document.findFirstElementByTagName("head", html);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        document.outerHtml("<html><body><head><title>Test</title></head><body><p>Test</p></body></html>");

        String html = document.outerHtml();
        assertEquals("<html><body><head><title>Test</title></head><body><p>Test</p></body></html>", html);
    }

    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.text("Test Text");

        Element body = document.body();
        assertEquals("Test Text", body.text());
    }
}