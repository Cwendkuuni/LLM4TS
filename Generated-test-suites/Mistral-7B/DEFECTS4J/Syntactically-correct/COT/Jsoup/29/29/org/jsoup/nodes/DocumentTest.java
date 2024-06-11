package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        Element html = doc.findFirstElementByTagName("html", doc);
        assertNotNull(html);
        Element head = html.findFirstElementByTagName("head", html);
        assertNotNull(head);
        Element body = html.findFirstElementByTagName("body", html);
        assertNotNull(body);
    }

    @Test
    public void testHeadAccessors() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testBodyAccessors() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        doc.text("Test Body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals("Test Body", body.text().trim());
    }

    @Test
    public void testElementCreation() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("", element.text().trim());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.head().appendElement("testTitle");
        doc.body().appendElement("testBody");
        doc.appendElement("testOutside");

        doc.normalise();

        Element html = doc.findFirstElementByTagName("html", doc);
        List<Element> heads = doc.getElementsByTag("head");
        List<Element> bodies = doc.getElementsByTag("body");

        assertEquals(1, heads.size());
        assertEquals("head", heads.get(0).nodeName());
        assertEquals("testTitle", heads.get(0).text().trim());

        assertEquals(1, bodies.size());
        assertEquals("body", bodies.get(0).nodeName());
        assertEquals("testBody", bodies.get(0).text().trim());

        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test
    public void testInvalidTagName() {
        Document doc = new Document("baseUri");
        try {
            doc.createElement("invalidTag");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateShellWithExistingHeadAndBody() {
        Document doc = new Document("baseUri");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Document shell = Document.createShell("baseUri");
        Element html = shell.findFirstElementByTagName("html", shell);
        Element newHead = html.findFirstElementByTagName("head", html);
        Element newBody = html.findFirstElementByTagName("body", html);

        assertEquals("head", newHead.nodeName());
        assertEquals("body", newBody.nodeName());

        List<Node> headNodes = head.childNodes();
        for (int i = 0; i < headNodes.size(); i++) {
            Node node = headNodes.get(i);
            Element newNode = newHead.appendChild(node.cloneNode(true));
            assertEquals(node.nodeName(), newNode.nodeName());
            assertEquals(node.text().trim(), newNode.text().trim());
        }

        List<Node> bodyNodes = body.childNodes();
        for (int i = 0; i < bodyNodes.size(); i++) {
            Node node = bodyNodes.get(i);
            Element newNode = newBody.appendChild(node.cloneNode(true));
            assertEquals(node.nodeName(), newNode.nodeName());
            assertEquals(node.text().trim(), newNode.text().trim());
        }
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), settings.charset());
        assertFalse(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());

        doc.outputSettings(new OutputSettings().prettyPrint(false).indentAmount(5));
        assertFalse(doc.outputSettings().prettyPrint());
        assertEquals(5, doc.outputSettings().indentAmount());
    }
}