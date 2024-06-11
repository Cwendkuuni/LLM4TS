package org.junit.test; // Replace with your test package

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DocumentTest {

    @Test
    public void testCreateDocumentMethod() {
        Document doc = Document.createShell("http://localhost:8080");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("http://localhost:8080");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("http://localhost:8080");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testTitleMethod() {
        Document doc = new Document("http://localhost:8080");
        String title = "Test Document";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodWithNullTitle() {
        Document doc = new Document("http://localhost:8080");
        doc.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("http://localhost:8080");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("#p", p.nodeName());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("http://localhost:8080");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendElement("p").text("Test Paragraph");
        Element textNode = doc.select("head title").text();
        doc.normalise();
        Element body = doc.body();
        List<Element> elements = body.children();
        assertEquals(2, elements.size());
        assertTrue(elements.get(0) instanceof TextNode);
        assertEquals("Test Title", elements.get(1).text());
        TextNode text = (TextNode) doc.select("p text").first();
        assertEquals("Test Paragraph", text.text());
    }

}