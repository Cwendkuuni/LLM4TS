package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(head);
        assertNotNull(body);
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertNull(element.parent());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element p = doc.createElement("p");
        TextNode textNode1 = doc.createTextNode("Text 1");
        TextNode textNode2 = doc.createTextNode("Text 2");
        p.appendChild(textNode1);
        p.appendChild(textNode2);
        doc.appendChild(p);

        TextNode textNode3 = doc.createTextNode("Text 3");
        doc.appendChild(textNode3);

        doc.normalise();

        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Element body = (Element) nodes.get(0);
        assertEquals(1, body.childNodes().size());
        Element pElement = (Element) body.childNodes().get(0);
        assertEquals(2, pElement.childNodes().size());
        TextNode textNode4 = (TextNode) pElement.childNodes().get(0);
        assertEquals("Text 1Text 2", textNode4.text());
        TextNode textNode5 = (TextNode) pElement.childNodes().get(1);
        assertEquals("Text 3", textNode5.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);

        Element p = doc.createElement("p");
        TextNode textNode1 = doc.createTextNode("Text 1");
        TextNode textNode2 = doc.createTextNode("Text 2");
        p.appendChild(textNode1);
        p.appendChild(textNode2);
        body.appendChild(p);

        Element script = doc.createElement("script").text("console.log('Hello World')");
        body.appendChild(script);

        Element h1 = doc.findFirstElementByTagName("h1", this);
        assertNull(h1);

        Element htmlElement = doc.findFirstElementByTagName("html", this);
        assertEquals(html, htmlElement);

        Element headElement = doc.findFirstElementByTagName("head", this);
        assertEquals(head, headElement);

        Element bodyElement = doc.findFirstElementByTagName("body", this);
        assertEquals(body, bodyElement);

        Element titleElement = doc.findFirstElementByTagName("title", this);
        assertEquals(title, titleElement);

        Element pElement = doc.findFirstElementByTagName("p", this);
        assertEquals(p, pElement);

        Element scriptElement = doc.findFirstElementByTagName("script", this);
        assertEquals(script, scriptElement);
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}