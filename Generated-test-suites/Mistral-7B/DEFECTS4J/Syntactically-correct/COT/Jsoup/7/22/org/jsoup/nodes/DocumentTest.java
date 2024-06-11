package org.jsoup.nodes; // make sure the test file is in the same package as the Document class

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
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("#root", doc.tagName());
        assertEquals(Tag.valueOf("#root"), doc.getRootElement().tagName());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(Tag.valueOf("html"), doc.tagName());
        Element html = doc.getRootElement();
        assertEquals(Tag.valueOf("head"), html.child(0).tagName());
        assertEquals(Tag.valueOf("body"), html.child(1).tagName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc = Document.createShell("http://example.com");
        head = doc.head();
        assertEquals(Tag.valueOf("head"), head.tagName());
        assertNotNull(head.text());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc = Document.createShell("http://example.com");
        body = doc.body();
        assertEquals(Tag.valueOf("body"), body.tagName());
        assertNotNull(body.text());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetter() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertEquals(Tag.valueOf("a"), element.tagName());
        assertEquals("", element.text());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body Text");
        doc.body().appendElement("p").text("Test Paragraph");

        doc.normalise();

        Element titleEl = doc.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text().trim());

        Element body = doc.body();
        assertEquals("Test Body Text", body.text().trim());
        assertEquals("Test Paragraph", body.getElementsByTag("p").first().text().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.findFirstElementByTagName("html", doc);
        assertEquals(Tag.valueOf("html"), html.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body Text");
        doc.body().appendElement("p").text("Test Paragraph");

        String htmlString = doc.outerHtml();
        assertTrue(htmlString.contains("<html>"));
        assertTrue(htmlString.contains("<head>"));
        assertTrue(htmlString.contains("<title>Test Title</title>"));
        assertTrue(htmlString.contains("<body>"));
        assertTrue(htmlString.contains("Test Body Text"));
        assertTrue(htmlString.contains("<p>Test Paragraph</p>"));
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendText("Test Body Text");

        doc.text("Test Text");
        assertEquals("Test Text", doc.outerHtml());

        doc.text("");
        assertEquals("<html><head><title></title><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertFalse(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());

        newOutputSettings = outputSettings.charset("ISO-8859-1");
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.charset());

        newOutputSettings = outputSettings.prettyPrint(false);
        assertNotSame(outputSettings, newOutputSettings);
        assertFalse(newOutputSettings.prettyPrint());

        newOutputSettings = outputSettings.indentAmount(5);
        assertNotSame(outputSettings, newOutputSettings);
        assertEquals(5, newOutputSettings.indentAmount());
    }

    
}