
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
        Element head = doc.createElement("head");
        doc.appendChild(head);
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
        Element body = doc.createElement("body");
        doc.appendChild(body);
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        Element meta = doc.createElement("meta");
        head.appendChild(meta);
        Element title = doc.createElement("title");
        head.appendChild(title);
        Element div = doc.createElement("div");
        body.appendChild(div);
        doc.normalise();
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals(meta, doc.head().child(0));
        assertEquals(title, doc.head().child(1));
        assertEquals(div, doc.body().child(0));
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.outerHtml());
        doc.normalise();
        assertEquals("<head><meta></meta></head><body><div></div></body>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc1 = new Document("http://example.com");
        Document doc2 = doc1.clone();
        assertNotNull(doc2);
        assertEquals(doc1.baseUri(), doc2.baseUri());
        assertEquals(doc1.nodeName(), doc2.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}
