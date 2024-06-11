package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("New Test Title");
        assertEquals("New Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("#a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        p.appendText("Test text");
        doc.body().appendChild(p);
        Element textNode = doc.getElementsByTag("textNode").first();
        assertNotNull(textNode);
        doc.normalise();
        Element body = doc.body();
        assertNotNull(body);
        List<Node> children = new ArrayList<>(body.childNodes());
        assertEquals(2, children.size());
        assertEquals("Test text", children.get(0).text());
        assertEquals("#text", children.get(0).tagName());
        assertEquals("", children.get(1).text());
        assertEquals("#document-fragment", children.get(1).tagName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element p = body.appendElement("p");
        p.appendText("Test text");

        Element found = doc.findFirstElementByTagName("html", this);
        assertEquals(html, found);

        found = doc.findFirstElementByTagName("head", this);
        assertEquals(head, found);

        found = doc.findFirstElementByTagName("body", this);
        assertEquals(body, found);

        found = doc.findFirstElementByTagName("title", this);
        assertEquals(title, found);

        found = doc.findFirstElementByTagName("p", this);
        assertEquals(p, found);

        found = doc.findFirstElementByTagName("textNode", this);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());

        doc.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());

        doc.outputSettings().charset("UTF-16LE");
        assertEquals("UTF-16LE", doc.outputSettings().charset().name());

        doc.outputSettings().charset("UTF-8");
        assertEquals("UTF-8", doc.outputSettings().charset().name());

        doc.outputSettings().prettyPrint(false);
        assertFalse(doc.outputSettings().prettyPrint());

        doc.outputSettings().indentAmount(2);
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}