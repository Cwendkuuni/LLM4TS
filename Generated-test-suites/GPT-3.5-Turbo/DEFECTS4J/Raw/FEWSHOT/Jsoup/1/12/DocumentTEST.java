
package org.jsoup.nodes;

import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.getElementById("html"));
        assertNotNull(doc.getElementById("head"));
        assertNotNull(doc.getElementById("body"));
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());

        Element head = doc.appendElement("head");
        assertNotNull(doc.head());
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());

        Element body = doc.appendElement("body");
        assertNotNull(doc.body());
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());

        Element title = doc.appendElement("title");
        title.text("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        Element html = doc.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");

        Element div = doc.createElement("div");
        div.text("Div Test");

        Element head = doc.head();
        head.appendChild(div);

        Element body = doc.body();
        body.appendElement("p");

        doc.normalise();

        assertEquals(2, doc.body().childNodes.size());
        assertNull(doc.head().getElementsByTag("div").first());
        assertNotNull(doc.body().getElementsByTag("div").first());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());

        doc.appendElement("head");
        doc.appendElement("body");

        assertEquals("<head></head><body></body>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");

        doc.text("Test Text");
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}
