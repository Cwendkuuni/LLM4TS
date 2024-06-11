package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {
    @Test
    public void testDocumentCreation() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertSame(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertSame(head, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertSame(body, found);

        found = doc.findFirstElementByTagName("title", doc); // should be null
        assertNull(found);
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element title = doc.appendElement("title").text("Test Title");
        String titleText = doc.title();
        assertEquals("Test Title", titleText);

        title.text("New Title");
        titleText = doc.title();
        assertEquals("New Title", titleText);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
        assertEquals(baseUri, newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        TextNode textNode1 = doc.appendTextNode("Test 1");
        TextNode textNode2 = doc.appendTextNode("Test 2");
        TextNode textNode3 = doc.appendTextNode("Test 3");

        head.appendElement("title").text("Test Title");

        doc.normalise();

        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        assertEquals(3, textNodes.size());
        assertEquals("Test 1", textNodes.get(0).text());
        assertEquals("Test 2", textNodes.get(1).text());
        assertEquals("Test 3", textNodes.get(2).text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertNotSame(outputSettings, newSettings);
        assertEquals(Entities.EscapeMode.extended, newSettings.escapeMode());
    }
}