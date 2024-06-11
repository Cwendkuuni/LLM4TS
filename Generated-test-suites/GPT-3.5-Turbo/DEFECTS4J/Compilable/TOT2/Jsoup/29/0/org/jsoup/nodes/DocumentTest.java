package org.jsoup.nodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head); // head element should be null initially
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNull(body); // body element should be null initially
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title()); // title should be empty initially
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://www.example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
        assertEquals("https://www.example.com", newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.normalise();
        // Add specific assertions for normalization
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.text("Test document text");
        assertEquals("Test document text", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("https://www.example.com");
        Document clone = doc.clone();
        assertNotNull(clone);
        assertNotSame(doc, clone);
        assertEquals(doc.baseUri(), clone.baseUri());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("UTF-8");
        doc.outputSettings(outputSettings);
        assertSame(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}