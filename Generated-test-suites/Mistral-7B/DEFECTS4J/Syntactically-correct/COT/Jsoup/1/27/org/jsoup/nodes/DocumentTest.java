package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals(3, document.childElementsSize());
    }

    @Test
    public void testHeadMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element head = document.head();
        assertEquals("head", head.tagName());
        assertSame(document, head.parent());
    }

    @Test
    public void testBodyMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element body = document.body();
        assertEquals("body", body.tagName());
        assertSame(document, body.parent());
    }

    @Test
    public void testTitleMethod() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSettingMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
        assertEquals("New Title", document.select("title").text());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleNullArgument() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Element newElement = document.createElement("a");
        assertEquals("a", newElement.tagName());
        assertEquals(document.baseUri(), newElement.baseUri());
        assertNull(newElement.parent());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test paragraph</p></body></html>");
        document.normalise();
        List<Element> bodyChildren = document.body().children();
        assertEquals(2, bodyChildren.size());
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testNormaliseWithEmptyDocument() {
        Document document = new Document("baseUri");
        document.normalise();
        List<Element> children = document.children();
        assertEquals(1, children.size());
        assertEquals("#document", children.get(0).tagName());
    }

    @Test
    public void testNormaliseWithInvalidHtml() {
        Document document = Jsoup.parse("<html><head></head>");
        document.appendElement("p");
        document.normalise();
        List<Element> bodyChildren = document.body().children();
        assertEquals(1, bodyChildren.size());
        assertEquals("p", bodyChildren.get(0).tagName());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test paragraph</p></body></html>");
        String outerHtml = document.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test paragraph</p></body></html>", outerHtml);
    }

    @Test
    public void testTextMethod() {
        Document document = Jsoup.parse("<html><head></head><body><p>Test paragraph</p></body></html>");
        document.body().text("New Text");
        assertEquals("New Text", document.text());
    }

    @Test
    public void testNodeNameMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        assertEquals("#document", document.nodeName());
    }
}