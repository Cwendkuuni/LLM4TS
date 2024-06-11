package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals(baseUri, doc.baseUri());
        Element html = doc.head();
        assertEquals("html", html.tagName());
        assertTrue(html.children().size() > 0);
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        assertTrue(body.children().size() == 0);
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        Element body = doc.body();

        assertNull(doc.title());
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
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element textNode1 = doc.createElement("textnode1").text("Text Node 1");
        Element textNode2 = doc.createElement("textnode2").text("Text Node 2");
        Element div = doc.createElement("div");
        div.appendChild(textNode1);
        doc.body().appendChild(div);
        doc.head().appendChild(textNode2);

        doc.normalise();

        List<Node> bodyNodes = new ArrayList<>();
        for (Node node : doc.body().childNodes()) {
            bodyNodes.add(node);
        }
        assertEquals(2, bodyNodes.size());
        assertEquals("Text Node 1", bodyNodes.get(0).text());
        assertEquals("Text Node 2", bodyNodes.get(1).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", body);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", html);
        assertSame(title, doc.titleElement());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("UTF-16");
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals("UTF-16", newOutputSettings.charset().name());
    }

    @Test
    public void testCreateTextNode() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        TextNode textNode = doc.createElement("textnode").text("Text Node");
        assertEquals("textnode", textNode.tagName());
        assertEquals("Text Node", textNode.outerHtml());
        assertEquals("Text Node", textNode.text());
    }
}