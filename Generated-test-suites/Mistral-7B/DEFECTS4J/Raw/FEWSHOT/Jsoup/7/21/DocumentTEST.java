package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertEquals("#html", html.nodeName());
        Element head = html.head();
        assertEquals("#head", head.nodeName());
        Element body = html.body();
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
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
        Element element = doc.createElement("a");
        assertEquals("#a", element.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.appendText("Hello ");
        doc.head().appendText(" World!");

        doc.normalise();

        Element body = doc.body();
        TextNode hello = (TextNode) body.childNodes().get(0);
        TextNode world = (TextNode) body.childNodes().get(1);

        assertEquals("Hello ", hello.text());
        assertEquals(" World!", world.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element headTitle = doc.findFirstElementByTagName("title", doc);
        assertEquals("#title", headTitle.nodeName());

        Element bodyP = doc.findFirstElementByTagName("p", body);
        assertNull(bodyP);

        doc.appendElement("p", body);
        Element bodyP2 = doc.findFirstElementByTagName("p", body);
        assertEquals("#p", bodyP2.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("US-ASCII");
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals("US-ASCII", newOutputSettings.charset().name());
        assertSame(outputSettings.encoder(), newOutputSettings.encoder());
        assertFalse(newOutputSettings.prettyPrint());
        assertEquals(1, newOutputSettings.indentAmount());
    }
}