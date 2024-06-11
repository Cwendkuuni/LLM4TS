
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);

        // Add head element
        doc.createElement("head");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);

        // Add body element
        doc.createElement("body");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);

        // Set title
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);

        // Test text normalization
        Element div = doc.createElement("div");
        div.appendText("Test Text");
        doc.appendChild(div);
        doc.normalise();

        assertEquals(1, head.childNodes.size());
        assertEquals(0, html.childNodes.size());
        assertEquals(2, body.childNodes.size());
        assertTrue(body.childNodes.get(0) instanceof TextNode);
        assertTrue(body.childNodes.get(1) instanceof TextNode);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("div");
        String html = doc.outerHtml();
        assertFalse(html.contains("<html>"));
        assertTrue(html.contains("<div></div>"));
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("body");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}
