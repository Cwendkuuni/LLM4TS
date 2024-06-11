package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetDocumentHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetDocumentBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetDocumentTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetDocumentTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateDocumentElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        doc.appendText("Test Before Body");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title 2");
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test Body 2");

        doc.normalise();

        assertEquals("html" , doc.getElementsByTag("html").first().nodeName());
        assertEquals("head" , doc.getElementsByTag("head").first().nodeName());
        assertEquals("title" , doc.getElementsByTag("title").first().nodeName());
        assertEquals("body" , doc.getElementsByTag("body").first().nodeName());

        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test Title", title.text());

        Element body = doc.getElementsByTag("body").first();
        assertEquals("Test Body", body.text());

        List<Node> children = body.childNodes();
        assertEquals(1, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("Test Body 2", children.get(0).text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        doc.outputSettings().prettyPrint(false);
        outputSettings = doc.outputSettings();
        assertFalse(outputSettings.prettyPrint());

        doc.outputSettings().charset("ISO-8859-1");
        outputSettings = doc.outputSettings();
        assertEquals("ISO-8859-1", outputSettings.charset().name());

        doc.outputSettings().indentAmount(2);
        outputSettings = doc.outputSettings();
        assertEquals(2, outputSettings.indentAmount());
    }
}