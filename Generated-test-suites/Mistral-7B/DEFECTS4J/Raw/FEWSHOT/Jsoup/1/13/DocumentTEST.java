package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc1 = new Document("http://example.com");
        assertEquals("#document", doc1.nodeName());
        assertNotNull(doc1.baseUri());
        assertEquals("", doc1.title());
        assertNull(doc1.head());
        assertNull(doc1.body());

        Document doc2 = Document.createShell("http://example.com");
        assertEquals(doc2, doc2.normalise());
        assertEquals("#document", doc2.nodeName());
        assertNotNull(doc2.baseUri());
        assertNotNull(doc2.head());
        assertNotNull(doc2.body());
    }

    @Test
    public void testGetHeadElement() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        Element head = doc.head();
        assertNotNull(head);
        Element title = head.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testGetBodyElement() {
        Element body = doc.body();
        assertNull(body);
        doc.appendText("Hello World!");
        body = doc.body();
        assertNotNull(body);
        assertEquals("Hello World!", body.text().trim());
    }

    @Test
    public void testSetTitle() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Element link = doc.createElement("a");
        assertEquals("a", link.nodeName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormalize() {
        doc.appendText("Hello ");
        doc.appendText("World!");
        doc.appendElement("p").text("This is a paragraph.");
        doc.head().appendElement("title").text("Test");

        doc.normalise();

        Element body = doc.body();
        assertEquals("Hello World!This is a paragraph.", body.text().trim());

        Element title = doc.head().getElementsByTag("title").first();
        assertEquals("Test", title.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeNullElement() {
        doc.normalise(null);
    }
}