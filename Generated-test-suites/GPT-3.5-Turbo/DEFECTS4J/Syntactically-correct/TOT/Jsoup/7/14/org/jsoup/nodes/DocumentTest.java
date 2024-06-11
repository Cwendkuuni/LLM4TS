package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("html", document.children().first().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Example Title");
        assertEquals("Example Title", document.title());

        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.normalise();
        assertEquals("html", document.children().first().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertTrue(outerHtml.isEmpty());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);

        assertSame(Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertSame(Entities.EscapeMode.extended, outputSettings.escapeMode());

        assertSame("UTF-8", outputSettings.charset().name());

        outputSettings.charset("ISO-8859-1");
        assertSame("ISO-8859-1", outputSettings.charset().name());

        assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}