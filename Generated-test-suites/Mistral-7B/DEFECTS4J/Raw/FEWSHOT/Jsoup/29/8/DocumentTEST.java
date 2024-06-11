package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Example Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = doc.createElement("head");
        Element bodyEl = doc.createElement("body");
        Element titleEl = doc.createElement("title");
        titleEl.text("Example Title");

        htmlEl.appendChild(headEl);
        htmlEl.appendChild(bodyEl);
        bodyEl.appendChild(titleEl);
        bodyEl.appendChild(doc.createTextNode("This is the body text."));

        doc.head().appendChild(titleEl);
        doc.body().appendChild(titleEl);

        doc.normalise();

        Element foundTitle = doc.getElementsByTag("title").first();
        assertEquals("Example Title", foundTitle.text());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("This is the body text.", doc.body().child(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = doc.createElement("head");
        Element bodyEl = doc.createElement("body");

        htmlEl.appendChild(headEl);
        htmlEl.appendChild(bodyEl);

        doc.head().appendChild(doc.createElement("title"));
        doc.body().appendChild(doc.createElement("div"));

        Element titleEl = doc.findFirstElementByTagName("title", this);
        assertEquals("title", titleEl.nodeName());

        Element divEl = doc.findFirstElementByTagName("div", this);
        assertEquals("div", divEl.nodeName());
    }

    @Test
    public void testTextMethod() {
        Document doc = new Document("http://example.com");
        Element bodyEl = doc.body();
        bodyEl.text("This is the body text.");
        assertEquals("This is the body text.", bodyEl.text());
    }

    @Test
    public void testCloneDocument() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element headEl = doc.createElement("head");
        Element bodyEl = doc.createElement("body");

        htmlEl.appendChild(headEl);
        htmlEl.appendChild(bodyEl);

        doc.head().appendChild(doc.createElement("title"));
        doc.body().appendChild(doc.createElement("div"));

        Document clonedDoc = doc.clone();
        Element clonedHtmlEl = clonedDoc.getElementsByTag("html").first();
        Element clonedHeadEl = clonedHtmlEl.getElementsByTag("head").first();
        Element clonedBodyEl = clonedHtmlEl.getElementsByTag("body").first();

        assertEquals(1, clonedHeadEl.childNodes().size());
        assertEquals(1, clonedBodyEl.childNodes().size());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended)
                .charset("ISO-8859-1")
                .prettyPrint(false)
                .indentAmount(2);

        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals("ISO-8859-1", newOutputSettings.charset().name());
        assertEquals(false, newOutputSettings.prettyPrint());
        assertEquals(2, newOutputSettings.indentAmount());

        doc.outputSettings(newOutputSettings);
    }
}