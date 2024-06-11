package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.title());
        assertTrue(doc.body() == null);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertSame(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        Element html = doc.findFirstElementByTagName("html");
        assertSame(html, doc);
        assertNotNull(html.head());
        assertNotNull(html.body());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element element = doc.createElement("div");
        assertEquals("#document", doc.nodeName());
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.head().appendElement("title").text("Test Title");
        doc.head().appendElement("title").text("Another Test Title");
        Element div = doc.createElement("div").text("Test Text");
        doc.body().appendElement("p").text("Original Text");
        doc.body().appendElement("p").text("Another Original Text");

        doc.normalise();

        Element title = doc.getElementsByTag("title").first();
        assertEquals(1, title.childNodes().size());
        assertEquals("Test Title", title.text());

        Element body = doc.body();
        assertEquals(3, body.childNodes().size());
        Element p1 = body.getElementsByTag("p").first();
        assertEquals("Original Text", p1.text());
        Element p2 = body.getElementsByTag("p").get(1);
        assertEquals("Another Original Text", p2.text());
        Element divElement = doc.getElementsByTag("div").first();
        assertEquals("Test Text", divElement.text());
    }
}