package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentInstance() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(3, doc.childNodes().size());
    }

    @Test
    public void testHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        assertEquals(2, doc.childNodes().size());
        assertEquals(head, doc.getElementsByTag("head").first());
        assertEquals(body, doc.getElementsByTag("body").first());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element link = doc.createElement("a");
        assertEquals("a", link.nodeName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendText("Hello World");
        doc.body().appendText(" Goodbye World");

        doc.normalise();

        assertEquals(3, doc.childNodes().size());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());
        assertEquals(" Goodbye World", doc.body().childNodes().get(1).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element firstHead = doc.findFirstElementByTagName("head", doc);
        Element firstBody = doc.findFirstElementByTagName("body", doc);

        assertEquals(head, firstHead);
        assertEquals(body, firstBody);
    }

    @Test
    public void testOuterHtmlAndText() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        p.text("Test");
        doc.body().appendChild(p);

        String html = doc.html();
        String text = doc.text();

        assertEquals("<html><head><title></title></head><body>Test</body></html>", html);
        assertEquals("Test", text);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());

        OutputSettings newSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("US-ASCII");
        assertEquals(Entities.EscapeMode.extended, newSettings.escapeMode());
        assertEquals("US-ASCII", newSettings.charset().name());

        assertEquals(newSettings, doc.outputSettings().charset("US-ASCII").escapeMode(Entities.EscapeMode.extended));
    }
}