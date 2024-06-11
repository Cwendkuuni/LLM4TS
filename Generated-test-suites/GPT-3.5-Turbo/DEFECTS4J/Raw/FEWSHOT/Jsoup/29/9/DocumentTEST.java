package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title(null);
        assertEquals("", doc.title());

        doc.title("    ");
        assertEquals("", doc.title());
    }

    @Test
    public void testcreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());

        Element html = doc.child(0);
        html.html("  ");
        doc.normalise();
        assertEquals("body", doc.child(0).nodeName());

        Element head = doc.head();
        head.html("  ");
        doc.normalise();
        assertEquals("body", doc.child(0).nodeName());

        doc.html("   ");
        doc.normalise();
        assertEquals("body", doc.child(0).nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());

        doc.text(null);
        assertEquals("", doc.body().text());

        doc.text("    ");
        assertEquals("", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertEquals(doc.baseUri(), clone.baseUri());
        assertTrue(clone.outputSettings() instanceof Document.OutputSettings);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        assertSame(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());

        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}