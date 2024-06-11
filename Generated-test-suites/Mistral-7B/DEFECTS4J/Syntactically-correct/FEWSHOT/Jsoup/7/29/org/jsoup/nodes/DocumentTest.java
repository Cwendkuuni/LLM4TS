package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.nio.charset.Charset;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");
        assertEquals("head", head.nodeName());
        assertEquals(doc, head.parent());
        assertNull(head.previousSibling());
        assertNull(head.nextSibling());
        assertEquals(0, head.childElementCount());

        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");
        assertEquals("body", body.nodeName());
        assertEquals(doc, body.parent());
        assertNull(body.previousSibling());
        assertNull(body.nextSibling());
        assertEquals(0, body.childElementCount());

        doc.body().appendElement("p").text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        Element title = doc.appendElement("head").appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", title.text());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
        assertEquals("New Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element a = doc.createElement("a");
        assertEquals("a", a.nodeName());
        assertEquals(doc.baseUri(), a.baseUri());
        assertNull(a.parent());
        assertNull(a.previousSibling());
        assertNull(a.nextSibling());
        assertEquals(0, a.childElementCount());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        body.appendText("Test ");
        Element p = doc.createElement("p").text("Text");
        body.appendChild(p);
        head.appendText("Test Head ");

        doc.normalise();

        assertEquals(3, doc.childElementCount());
        assertEquals(1, html.childElementCount());
        assertEquals(2, html.firstChild().childElementCount());
        assertEquals("Test Head ", head.text());
        assertEquals("Test Text", body.child(1).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element h1 = doc.createElement("h1").text("Test");
        head.appendChild(h1);

        Element h1Result = doc.findFirstElementByTagName("h1", this);
        assertSame(h1, h1Result);

        Element pResult = doc.findFirstElementByTagName("p", this);
        assertNull(pResult);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), settings.charset());
        assertTrue(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());
    }

    @Test
    public void testOutputSettingsWithCustomValues() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        settings.escapeMode(Entities.EscapeMode.extended);
        settings.charset(Charset.forName("ISO-8859-1"));
        settings.prettyPrint(false);
        settings.indentAmount(4);

        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), settings.charset());
        assertFalse(settings.prettyPrint());
        assertEquals(4, settings.indentAmount());
    }
}