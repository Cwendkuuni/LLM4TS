package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {
    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        Element head = html.head();
        Element body = html.body();
        assertEquals(Tag.valueOf("html"), html.tag());
        assertEquals(Tag.valueOf("head"), head.tag());
        assertEquals(Tag.valueOf("body"), body.tag());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testGetHeadElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        assertNotNull(head);
        assertEquals(Tag.valueOf("head"), head.tag());
    }

    @Test
    public void testGetBodyElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        assertNotNull(body);
        assertEquals(Tag.valueOf("body"), body.tag());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        document.title("New Test Title");
        String title = document.title();
        assertEquals("New Test Title", title);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("p");
        assertEquals(Tag.valueOf("p"), element.tag());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.head().appendElement("title").text("Test Title");
        document.head().appendElement("title").text("Another Test Title");
        document.text("Test text");

        document.normalise();

        Element title = document.head().firstElementSibling();
        assertEquals("Test Title", title.text());
        assertEquals(Tag.valueOf("title"), title.tag());

        Element body = document.body();
        assertEquals("Test text", body.text());
        assertEquals(Tag.valueOf("body"), body.tag());
    }
}