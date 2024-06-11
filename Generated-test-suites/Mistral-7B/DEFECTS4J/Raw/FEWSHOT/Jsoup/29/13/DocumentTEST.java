package org.jsoup.nodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document shellDoc = Document.createShell(baseUri);
        assertEquals(Tag.valueOf("html"), shellDoc.tag());
        Element html = shellDoc.head();
        assertEquals(Tag.valueOf("head"), html.tag());
        html = shellDoc.body();
        assertEquals(Tag.valueOf("body"), html.tag());
        assertNull(shellDoc.title());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        Element head = doc.head();
        Element body = doc.body();
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element element = doc.createElement("a");
        assertEquals(Tag.valueOf("a"), element.tag());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.body().text("Test");
        doc.head().appendElement("title").text("Test Title");
        doc.appendElement("html").appendElement("head").text("Test Head");
        doc.appendElement("html").appendElement("body").text("Test Body");

        doc.normalise();

        Element title = doc.head().getElementsByTag("title").first();
        Element body = doc.body();
        assertEquals("Test Title", title.text().trim());
        assertArrayEquals(new String[]{"Test"}, body.text().split("\\s+"));
    }

    @Test
    public void testNormaliseDocumentWithEmptyBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.head().appendElement("title").text("Test Title");
        doc.appendElement("html").appendElement("head").text("Test Head");
        doc.appendElement("html").appendElement("body");

        doc.normalise();

        Element title = doc.head().getElementsByTag("title").first();
        Element body = doc.body();
        assertEquals("Test Title", title.text().trim());
        assertNull(body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element element = doc.findFirstElementByTagName("html", this);
        assertSame(html, element);

        element = doc.findFirstElementByTagName("body", this);
        assertSame(body, element);

        element = doc.findFirstElementByTagName("title", this);
        assertNull(element);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.body().text("Test");
        doc.head().appendElement("title").text("Test Title");

        String html = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body>Test</body></html>", html);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.body().text("Test");

        String text = doc.text();
        assertEquals("Test", text);
    }

    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCloneDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));
        doc.head().appendElement("title").text("Test Title");

        Document clone = doc.clone();
        assertEquals("Test Title", clone.title());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));

        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        assertEquals(OutputSettings.Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));

        OutputSettings outputSettings = new OutputSettings();
        outputSettings.escapeMode(OutputSettings.Entities.EscapeMode.extended);
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);

        doc.outputSettings(outputSettings);

        OutputSettings docOutputSettings = doc.outputSettings();
        assertEquals(OutputSettings.Entities.EscapeMode.extended, docOutputSettings.escapeMode());
        assertEquals("ISO-8859-1", docOutputSettings.charset().name());
        assertFalse(docOutputSettings.prettyPrint());
        assertEquals(2, docOutputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));

        doc.quirksMode(QuirksMode.quirks);

        assertEquals(QuirksMode.quirks, doc.quirksMode());
    }

    @Test
    public void testInvalidQuirksMode() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri).appendChild(Document.createShell(baseUri));

        assertThrows(IllegalArgumentException.class, () -> doc.quirksMode(QuirksMode.invalid));
    }
}