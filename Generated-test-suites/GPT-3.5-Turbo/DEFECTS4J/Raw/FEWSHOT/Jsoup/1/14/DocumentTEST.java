

package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("https://www.example.com");
        document.title("Example");
    }

    @Test
    public void testConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://www.example.com";
        Document doc = Document.createShell(baseUri);

        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.getElementsByTag("html").first().nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("Example", title);
    }

    @Test
    public void testSetTitle() {
        document.title("NewTitle");
        String title = document.title();
        assertEquals("NewTitle", title);
    }

    @Test
    public void testSetTitle_AddsToHeadIfNotPresent() {
        Document doc = Document.createShell("https://www.example.com");
        String title = "Title";

        doc.title(title);
        assertEquals(title, doc.title());
        assertNotNull(doc.head().getElementsByTag("title").first());
        assertEquals(title, doc.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitle_UpdatesExistingTitleElement() {
        String title = "OriginalTitle";
        document.title(title);

        String newTitle = "NewTitle";
        document.title(newTitle);

        assertEquals(newTitle, document.title());
        assertNotNull(document.head().getElementsByTag("title").first());
        assertEquals(newTitle, document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "a";
        Element element = document.createElement(tagName);
        assertNotNull(element);
        assertEquals(tagName, element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise_WithEmptyDocument() {
        Document emptyDocument = new Document("https://www.example.com");
        emptyDocument.normalise();

        assertFalse(emptyDocument.select("html").isEmpty());
        assertNotNull(emptyDocument.head());
        assertNotNull(emptyDocument.body());
    }

    @Test
    public void testNormalise_AddsHeadIfNotPresent() {
        Document noHeadDocument = new Document("https://www.example.com");
        noHeadDocument.appendElement("html");
        noHeadDocument.appendElement("body");

        assertNull(noHeadDocument.head());

        noHeadDocument.normalise();

        assertNotNull(noHeadDocument.head());
    }

    @Test
    public void testNormalise_AddsBodyIfNotPresent() {
        Document noBodyDocument = new Document("https://www.example.com");
        noBodyDocument.appendElement("html");
        noBodyDocument.prependElement("head");

        assertNull(noBodyDocument.body());

        noBodyDocument.normalise();

        assertNotNull(noBodyDocument.body());
    }

    @Test
    public void testNormalise_MovesTextNodesToBody() {
        Document document = new Document("https://www.example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendText("Text Node 1");
        html.appendElement("div");
        html.appendText("Text Node 2");

        assertNull(document.body().text());

        document.normalise();

        assertEquals("Text Node 1 Text Node 2", document.body().text());
    }

    @Test
    public void testNormalise_WithNonTextNode() {
        Document document = new Document("https://www.example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("div");

        assertNull(document.body().text());

        document.normalise();

        assertEquals("", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://www.example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");

        String outerHtml = document.outerHtml();
        assertEquals("<html><head></head><body></body></html>", outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("https://www.example.com");
        Element body = document.appendElement("body");

        document.text("Example Text");
        assertEquals("Example Text", body.text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}

