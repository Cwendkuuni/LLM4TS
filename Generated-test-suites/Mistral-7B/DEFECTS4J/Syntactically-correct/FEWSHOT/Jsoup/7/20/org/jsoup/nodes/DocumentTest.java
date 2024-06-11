package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNull(html);
        html = doc.body();
        assertNull(html);
        Element head = doc.head();
        Element body = doc.body();
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertNull(head);
        assertNull(body);
        Element html = doc.appendElement("html");
        head = doc.head();
        body = doc.body();
        assertSame(html, head.parentNode());
        assertSame(html, body.parentNode());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        TextNode textNode = doc.appendTextNode("Test text");
        Element div = doc.createElement("div");
        doc.appendChild(div);
        doc.appendChild(textNode);

        doc.normalize();

        List<Node> children = new ArrayList<>();
        for (Node node : html.childNodes()) {
            children.add(node);
        }
        assertEquals(4, children.size());
        assertSame(children.get(0), html);
        assertSame(children.get(1), head);
        assertSame(children.get(2), body);
        assertSame(children.get(3), textNode);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element found = doc.findFirstElementByTagName("html", this);
        assertEquals(html, found);

        found = doc.findFirstElementByTagName("head", this);
        assertEquals(head, found);

        found = doc.findFirstElementByTagName("body", this);
        assertEquals(body, found);

        found = doc.findFirstElementByTagName("script", this);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().name());

        doc.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());

        doc.outputSettings().prettyPrint(false);
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testSetText() {
        Document doc = new Document("http://example.com");
        doc.text("Test");
        assertEquals("Test", doc.text().get());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}