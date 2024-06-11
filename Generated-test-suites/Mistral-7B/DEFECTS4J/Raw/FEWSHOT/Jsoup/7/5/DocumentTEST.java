package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocumentWithBaseUri() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertEquals(3, doc.childNodeSize());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertSame(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertSame(head, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertSame(body, found);

        found = doc.findFirstElementByTagName("script", doc);
        assertNull(found);
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("http://example.com");
        Element h1 = doc.createElement("h1").text("Test");
        Element p1 = doc.createElement("p").text("First paragraph.");
        Element p2 = doc.createElement("p").text("Second paragraph.");

        doc.body().appendChild(h1);
        doc.body().appendChild(p1);
        doc.body().appendChild(p2);
        doc.head().appendChild(h1);

        doc.normalise();

        assertEquals(3, doc.childNodeSize());
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(2, doc.body().childNodeSize());

        List<Node> bodyTextNodes = doc.body().childNodes();
        assertEquals(2, bodyTextNodes.size());

        Node firstBodyTextNode = bodyTextNodes.get(0);
        assertTrue(firstBodyTextNode instanceof TextNode);
        assertEquals("", firstBodyTextNode.text());

        Node secondBodyTextNode = bodyTextNodes.get(1);
        assertTrue(secondBodyTextNode instanceof TextNode);
        assertEquals("Test\nFirst paragraph.\nSecond paragraph.\n", secondBodyTextNode.text());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleSetNull() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element link = doc.createElement("a");
        assertEquals("a", link.nodeName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8(), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings outputSettingsWithPrettyPrintFalse = doc.outputSettings().prettyPrint(false);
        assertFalse(outputSettingsWithPrettyPrintFalse.prettyPrint());

        OutputSettings outputSettingsWithIndentAmount3 = doc.outputSettings().indentAmount(3);
        assertEquals(3, outputSettingsWithIndentAmount3.indentAmount());
    }
}