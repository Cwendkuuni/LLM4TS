package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author John Doe
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://www.example.com");
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNull(document.title());
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals(Tag.HTML, document.tagName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNull(document.title());
        assertEquals(3, document.childNodes().size());
        assertEquals(Tag.HEAD, document.head().tagName());
        assertEquals(Tag.BODY, document.body().tagName());
    }

    @Test
    public void testAccessHeadAndBody() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.head();
        Element body = document.body();

        // Test head accessor
        assertSame(head, document.head());
        assertEquals(Tag.HEAD, head.tagName());
        assertEquals(0, head.childNodes().size());

        // Test body accessor
        assertSame(body, document.body());
        assertEquals(Tag.BODY, body.tagName());
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://www.example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals("Test Title", document.head().childNodes().get(0).text());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://www.example.com");
        Element p = document.createElement("p");
        assertEquals(Tag.P, p.tagName());
        assertEquals("http://www.example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.appendElement("head");
        Element title = document.createElement("title").text("Test Title");
        head.appendChild(title);
        Element body = document.appendElement("body");
        TextNode text = document.createTextNode("This is a test.");
        body.appendChild(text);
        text = document.createTextNode(" Another test.");
        body.appendChild(text);

        document.normalise();

        assertEquals(3, document.childNodes().size());
        assertEquals(Tag.HTML, document.tagName());
        assertEquals(Tag.HEAD, document.head().tagName());
        assertEquals(Tag.BODY, document.body().tagName());

        assertEquals(1, document.head().childNodes().size());
        assertEquals("Test Title", document.head().childNodes().get(0).text());

        assertEquals(1, document.body().childNodes().size());
        assertEquals("This is a test. Another test.", document.body().text());
    }

    @Test
    public void testNormaliseStructure() {
        Document document = Document.createShell("http://www.example.com");
        Element head1 = document.appendElement("head");
        Element head2 = document.appendElement("head");
        Element body = document.appendElement("body");
        TextNode text1 = document.createTextNode("Text 1");
        TextNode text2 = document.createTextNode("Text 2");

        head1.appendChild(text1);
        head2.appendChild(text2);
        document.appendChild(head1);
        document.appendChild(head2);
        document.appendChild(body);

        document.normalise();

        assertEquals(3, document.childNodes().size());
        assertEquals(Tag.HTML, document.tagName());

        assertEquals(1, document.head().childNodes().size());
        assertEquals("Text 1", document.head().childNodes().get(0).text());

        assertEquals(1, document.body().childNodes().size());
        assertEquals("Text 2", document.body().text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.head();
        Element body = document.body();

        Element title = document.findFirstElementByTagName("title", head);
        assertNull(title);

        title = document.findFirstElementByTagName("title", body);
        assertNull(title);

        title = document.findFirstElementByTagName("title", document);
        assertEquals("", title.text());

        Element html = document.findFirstElementByTagName("html", document);
        assertEquals(Tag.HTML, html.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.appendElement("head");
        Element title = document.createElement("title").text("Test Title");
        head.appendChild(title);
        Element body = document.appendElement("body").text("Test Body");

        document.normalise();

        String outerHtml = document.outerHtml();
        String expected = "<html><head><title></title></head><body>Test Body</body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://www.example.com");
        Element body = document.appendElement("body").text("Test Body");

        document.text("Test Text");

        assertEquals("Test Text", document.text());
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testClone() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        TextNode text1 = document.createTextNode("Text 1");
        TextNode text2 = document.createTextNode("Text 2");

        head.appendChild(text1);
        body.appendChild(text2);
        document.appendChild(head);
        document.appendChild(body);

        Document clonedDocument = document.clone();

        assertEquals(document.title(), clonedDocument.title());
        assertEquals(document.outputSettings().prettyPrint(), clonedDocument.outputSettings().prettyPrint());
        assertEquals(document.outputSettings().charset().name(), clonedDocument.outputSettings().charset().name());
        assertEquals(document.outputSettings().escapeMode().toString(), clonedDocument.outputSettings().escapeMode().toString());

        assertEquals(document.childNodes().size(), clonedDocument.childNodes().size());
        assertEquals(document.head(), clonedDocument.head());
        assertEquals(document.body(), clonedDocument.body());

        assertEquals(document.head().childNodes().get(0).text(), clonedDocument.head().childNodes().get(0).text());
        assertEquals(document.body().childNodes().get(0).text(), clonedDocument.body().childNodes().get(0).text());

        TextNode text1Clone = (TextNode) clonedDocument.head().childNodes().get(0);
        TextNode text2Clone = (TextNode) clonedDocument.body().childNodes().get(0);

        assertEquals(text1.text(), text1Clone.text());
        assertEquals(text2.text(), text2Clone.text());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://www.example.com");
        OutputSettings outputSettings = new OutputSettings();
        document.outputSettings(outputSettings);

        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = Document.createShell("http://www.example.com");
        QuirksMode quirksMode = QuirksMode.quirks;
        document.quirksMode(quirksMode);

        assertEquals(quirksMode, document.quirksMode());
    }
}