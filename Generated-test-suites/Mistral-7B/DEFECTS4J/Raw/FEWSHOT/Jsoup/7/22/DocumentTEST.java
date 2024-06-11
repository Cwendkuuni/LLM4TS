package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testNewDocument() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
        assertNull(document.title());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        assertEquals("html", html.nodeName());
        Element head = document.head();
        assertEquals("head", head.nodeName());
        Element body = document.body();
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testAccessors() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        Element head = document.head();
        Element title = head.appendElement("title");
        title.text("Test Title X");
        assertEquals("Test Title X", document.title());

        Element body = document.body();
        TextNode textNode = body.appendText("Test body text");
        assertEquals("Test body text", document.text());
        assertEquals("Test body text\n", document.outerHtml());

        Document normalisedDocument = document.normalise();
        assertEquals("Test body text\n", normalisedDocument.text());
        assertEquals("Test body text\n", normalisedDocument.outerHtml());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element p1 = document.createElement("p");
        Element p2 = document.createElement("p");
        TextNode textNode1 = p1.appendText("Text node 1");
        TextNode textNode2 = p2.appendText("Text node 2");

        document.appendChild(p1);
        document.appendChild(p2);

        document.normalise();

        List<Node> bodyChildren = document.body().childNodes();
        int indexOfTextNode1 = bodyChildren.indexOf(textNode1);
        int indexOfTextNode2 = bodyChildren.indexOf(textNode2);
        assertEquals(0, indexOfTextNode1);
        assertEquals(1, indexOfTextNode2);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = head.appendElement("title");
        title.text("Test Title");

        Element p = body.appendElement("p");
        p.text("Test text");

        Element found = document.findFirstElementByTagName("title", document);
        assertEquals("title", found.nodeName());
        assertEquals("Test Title", found.text());

        found = document.findFirstElementByTagName("p", document);
        assertEquals("p", found.nodeName());
        assertEquals("Test text", found.text());

        found = document.findFirstElementByTagName("div", document);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertNotNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals(outputSettings, document.outputSettings());
    }
}