package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {
    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(3, doc.childNodes().size());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        Element title = doc.head().appendElement("title");
        title.text("Original Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        assertEquals("Original Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        doc.appendChild(p);
        TextNode textNode = doc.createTextNode("Test Text");
        p.appendChild(textNode);
        doc.normalise();
        Element body = doc.body();
        List<Node> bodyChildren = new ArrayList<>(body.childNodes());
        assertSame(textNode, bodyChildren.get(0));
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title");
        title.text("Test Title");
        html.appendChild(head);
        head.appendChild(title);
        html.appendChild(body);

        Element foundTitle = doc.findFirstElementByTagName("title", this);
        assertSame(title, foundTitle);
    }

    @Test
    public void testOutputSettings_EscapeMode() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());

        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }

    @Test
    public void testOutputSettings_Charset() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(StandardCharsets.UTF_8, settings.charset());

        settings.charset(StandardCharsets.ISO_8859_1);
        assertEquals(StandardCharsets.ISO_8859_1, settings.charset());
    }

    @Test
    public void testOutputSettings_PrettyPrint() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertTrue(settings.prettyPrint());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
    }

    @Test
    public void testOutputSettings_IndentAmount() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(1, settings.indentAmount());

        settings.indentAmount(4);
        assertEquals(4, settings.indentAmount());
    }
}