package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);
        
        head = doc.createElement("head");
        doc.appendChild(head);
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);
        
        body = doc.createElement("body");
        doc.appendChild(body);
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);
        
        doc.title("My Title");
        title = doc.title();
        assertEquals("My Title", title);
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("http://www.example.com");
        doc.normalise();
        assertEquals("<html><head></head><body><text></text></body></html>", doc.outerHtml());
        
        Element head = doc.createElement("head");
        doc.appendChild(head);
        doc.normalise();
        assertEquals("<html><head></head><body><text></text></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.text("My Text");
        assertEquals("<html><head></head><body>My Text</body></html>", doc.outerHtml());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }
}