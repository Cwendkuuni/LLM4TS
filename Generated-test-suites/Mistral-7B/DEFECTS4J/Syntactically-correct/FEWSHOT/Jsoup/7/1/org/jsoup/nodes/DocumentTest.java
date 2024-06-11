package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());

        Element html = doc.head();
        assertEquals("html", html.nodeName());

        Element head = html;
        assertEquals("head", head.nodeName());

        Element body = head;
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Document");
        assertEquals("Test Document", doc.title());

        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);

        Element html = doc.appendElement("html");
        assertEquals("html", html.nodeName());
        html.appendElement("head");
        Element head = html.appendElement("head");
        head.appendElement("title").text("Test Document");
        html.appendElement("body");
        Element body = html.appendElement("body");
        body.appendElement(element);

        assertEquals("div", element.nodeName());
        assertEquals("http://example.com/div", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.title("Test Document");
        Element title = doc.getElementsByTag("title").first();

        doc.appendElement("html").appendElement("head").appendElement("title").text("New Title");
        doc.appendElement("body").appendText("Test text");

        doc.normalise();

        assertEquals("Test Document", title.text());
        assertEquals("Test text", doc.body().text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = findFirstElementByTagName(doc, "title");
        assertEquals("title", title.nodeName());

        title = findFirstElementByTagName(head, "title");
        assertEquals("title", title.nodeName());

        title = findFirstElementByTagName(body, "title");
        assertNull(title);

        title = findFirstElementByTagName(html, "body");
        assertNull(title);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertFalse(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newSettings = outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", newSettings.charset().name());

        OutputSettings newSettings2 = newSettings.prettyPrint(true);
        assertTrue(newSettings2.prettyPrint());
    }

    private Element findFirstElementByTagName(Document doc, String tag) {
        return doc.findFirstElementByTagName(tag, doc);
    }
}