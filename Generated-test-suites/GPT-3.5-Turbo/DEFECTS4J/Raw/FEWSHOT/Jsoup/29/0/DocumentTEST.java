package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);

        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Example Page");
        title = doc.title();
        assertEquals("Example Page", title);
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        doc.appendElement("html").appendElement("head");
        doc.appendElement("head").text("This is the head");
        doc.appendElement("body").text("This is the body");

        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = doc.getElementsByTag("head").first();
        assertNull(head);
        Element body = doc.getElementsByTag("body").first();
        assertNull(body);

        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://www.example.com");
        doc.appendElement("body").text("This is the body");

        doc.text("This is the new body");

        Element body = doc.body();
        assertNotNull(body);
        assertEquals("This is the new body", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        doc.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, doc.quirksMode());
    }
}