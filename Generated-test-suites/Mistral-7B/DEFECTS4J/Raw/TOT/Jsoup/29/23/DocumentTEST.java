package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Set up any necessary objects or data here
    }

    @org.junit.After
    public void tearDown() {
        // Clean up any objects or data created in setUp() here
    }

    @org.junit.Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @org.junit.Test
    public void testCreateShellDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
    }

    @org.junit.Test
    public void testGetHeadElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("head", head.tagName().name());
        assertEquals(doc, head.parent());
        assertEquals(0, doc.children().size());
    }

    @org.junit.Test
    public void testGetBodyElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName().name());
        assertEquals(doc, body.parent());
        assertEquals(0, doc.children().size());
    }

    @org.junit.Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Document");
        assertEquals("Test Document", doc.title());
    }

    @org.junit.Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Document");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document doc = new Document("");
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName().name());
        assertEquals(doc.baseUri(), element.baseUri());
        assertNull(element.parent());
    }

    @org.junit.Test
    public void testNormaliseDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title").text("Test Document");
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p").text("This is a test.");
        body.appendChild(p);

        doc.normalise();

        assertEquals(1, doc.children().size());
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertEquals("Test Document", doc.title());
        assertEquals(1, doc.head().childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("p", p.tagName().name());
        assertEquals("This is a test.", p.text());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentNullBaseUri() {
        new Document(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateShellDocumentNullBaseUri() {
        Document.createShell(null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document doc = new Document("");
        doc.findFirstElementByTagName("html", null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTag() {
        Document doc = new Document("");
        doc.findFirstElementByTagName("invalid", doc);
    }

    @org.junit.Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title").text("Test Document");
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p").text("This is a test.");
        body.appendChild(p);

        doc.normalise();

        StringBuilder expected = new StringBuilder();
        expected.append("<html><head></head><body><p>This is a test.</p></body></html>")
                .appendSystemLineSeparator();
        assertEquals(expected.toString(), doc.outerHtml());
    }

    @org.junit.Test
    public void testText() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        body.text("This is a test.");
        assertEquals("This is a test.", doc.text());
    }

    @org.junit.Test
    public void testClone() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title").text("Test Document");
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p").text("This is a test.");
        body.appendChild(p);

        Document clonedDoc = doc.clone();
        assertEquals(1, clonedDoc.children().size());
        assertEquals("#document", clonedDoc.nodeName());
        assertEquals(baseUri, clonedDoc.baseUri());
        assertEquals("Test Document", clonedDoc.title());
        assertEquals(1, clonedDoc.head().childNodes().size());
        assertEquals(1, clonedDoc.body().childNodes().size());
        assertEquals("p", p.tagName().name());
        assertEquals("This is a test.", p.text());
    }

    @org.junit.Test
    public void testOutputSettings() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);

        OutputSettings outputSettings = new OutputSettings();
        outputSettings.escapeMode(OutputSettings.Entities.EscapeMode.extended);
        doc.outputSettings(outputSettings);

        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title").text("Test Document");
        Element script = doc.createElement("script").src("script.js");
        head.appendChild(title);
        head.appendChild(script);

        StringBuilder expected = new StringBuilder();
        expected.append("<html><head>")
                .append("<title>Test Document</title>")
                .append("<script src=\"script.js\"></script>")
                .append("</head><body></body></html>")
                .appendSystemLineSeparator();

        assertEquals(expected.toString(), doc.outerHtml());
    }
}