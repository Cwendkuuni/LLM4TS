package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

    Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals(Tag.valueOf("#document"), doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals(Tag.valueOf("html"), doc.head().tagName());
        assertEquals(Tag.valueOf("head"), doc.head().firstChild().tagName());
        assertEquals(Tag.valueOf("body"), doc.body().tagName());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testHeadAccess() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals(Tag.valueOf("head"), head.tagName());
    }

    @Test
    public void testBodyAccess() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals(Tag.valueOf("body"), body.tagName());
    }

    @Test
    public void testTitleAccess() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleSetNull() {
        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testTitleSetEmptyString() {
        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Element newElement = document.createElement("p");
        assertNotNull(newElement);
        assertEquals(Tag.valueOf("p"), newElement.tagName());
        assertEquals("http://example.com", newElement.baseUri());
    }

    @Test
    public void testDocumentNormalize() {
        document.title("Test Title");
        Element pElement = document.createElement("p");
        pElement.text("Test Text");
        document.body().appendChild(pElement);

        document.normalise();

        assertEquals("Test Title", document.title());
        assertEquals(1, document.childNodeSize());
        assertEquals(Tag.valueOf("html"), document.head().parent().tagName());
        assertEquals(Tag.valueOf("head"), document.head().tagName());
        assertEquals(Tag.valueOf("body"), document.body().tagName());
        assertEquals(Tag.valueOf("p"), document.body().firstChild().tagName());
        assertEquals("Test Text", document.body().firstChild().text());
    }

    @Test
    public void testTextNodeNormalize() {
        document.title("Test Title");
        Element pElement = document.createElement("p");
        pElement.text("Test Text ");
        document.body().appendChild(pElement);

        document.normalise();

        assertEquals("Test Title", document.title());
        assertEquals(1, document.childNodeSize());
        Element body = document.body();
        assertEquals(Tag.valueOf("html"), body.parent().tagName());
        assertEquals(Tag.valueOf("body"), body.tagName());
        assertEquals(2, body.childNodeSize());
        TextNode textNode1 = (TextNode) body.child(0);
        assertEquals("Test ", textNode1.text());
        TextNode textNode2 = (TextNode) body.child(1);
        assertEquals("Text", textNode2.text());
    }

    @Test
    public void testDocumentClone() {
        document.title("Test Title");
        Element pElement = document.createElement("p");
        pElement.text("Test Text");
        document.body().appendChild(pElement);
        Document clonedDocument = document.clone();

        assertEquals("Test Title", document.title());
        assertEquals("Test Text", document.body().getText());

        assertEquals("Test Title", clonedDocument.title());
        assertEquals("Test Text", clonedDocument.body().getText());
        assertNotSame(document, clonedDocument);
    }
}