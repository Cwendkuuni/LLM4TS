package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().tagName().equals("head"));
        assertTrue(doc.body().tagName().equals("body"));
        assertEquals("", doc.title());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Title");
        assertEquals("Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        assertNull(doc.title());

        doc.title("Title");
        assertEquals("Title", doc.title());

        doc.title("Title1");
        assertEquals("Title1", doc.title());

        doc.title("Title2");
        assertEquals("Title2", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals(3, doc.childNodes().size());

        doc.normalise();
        assertEquals(3, doc.childNodes().size());
        assertEquals("#document", doc.childNodes().get(0).nodeName());
        assertEquals("html", doc.childNodes().get(1).nodeName());
        assertEquals("head", doc.childNodes().get(2).nodeName());

        Element head = doc.head();
        Element title = head.appendElement("title");
        title.text("Title");

        doc.normalise();
        assertEquals("html", doc.childNodes().get(1).nodeName());
        Element body = doc.body();
        assertEquals("head", body.previousElementSibling().nodeName());
        assertEquals("title", body.previousElementSibling().childNodes().get(0).nodeName());
        assertEquals("Title", body.previousElementSibling().childNodes().get(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = head.appendElement("title");
        title.text("Title");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertSame(html, found);

        found = doc.findFirstElementByTagName("title", doc);
        assertSame(title, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertSame(body, found);

        found = doc.findFirstElementByTagName("foo", doc);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertNotNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());

        newOutputSettings = outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.charset());

        newOutputSettings = outputSettings.charset("ISO-8859-1");
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.charset());

        newOutputSettings = outputSettings.prettyPrint(false);
        assertNotSame(outputSettings, newOutputSettings);
        assertFalse(newOutputSettings.prettyPrint());

        newOutputSettings = outputSettings.indentAmount(2);
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(2, newOutputSettings.indentAmount());
    }
}