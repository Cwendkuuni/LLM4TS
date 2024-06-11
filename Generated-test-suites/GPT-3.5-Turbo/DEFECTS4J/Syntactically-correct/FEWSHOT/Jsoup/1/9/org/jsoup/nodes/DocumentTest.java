package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitle_Null() {
        Document doc = new Document("https://www.example.com");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testTitle_AddToHead() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitle_UpdateExistingElement() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.head().appendElement("title").text("Old Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testTitle_UpdateExistingElement_Null() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.head().appendElement("title").text("Old Title");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testnormalise() {
        Document doc = new Document("https://www.example.com");
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testouterHtml() {
        Document doc = new Document("https://www.example.com");
        String outerHtml = doc.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("", outerHtml);
    }

    @Test
    public void testtext() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test");
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testnodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}