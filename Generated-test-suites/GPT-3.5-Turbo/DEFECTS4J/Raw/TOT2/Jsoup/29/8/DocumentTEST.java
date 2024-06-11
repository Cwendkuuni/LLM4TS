package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        doc.addElement("div").text("Test");
        doc.normalise();
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://www.example.com");
        Document clonedDoc = doc.clone();
        assertNotNull(clonedDoc);
        assertNotSame(doc, clonedDoc);
        assertEquals(doc.outputSettings().charset(), clonedDoc.outputSettings().charset());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings().charset("ISO-8859-1").prettyPrint(false);
        doc.outputSettings(outputSettings);
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }

}