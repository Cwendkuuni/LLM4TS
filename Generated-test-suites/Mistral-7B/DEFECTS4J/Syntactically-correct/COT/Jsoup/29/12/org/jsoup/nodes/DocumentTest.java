package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }

    /**
     * Test the createShell method.
     */
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        assertSame(Tag.HTML, html.tag());
        Element head = html.getFirstElementChild();
        assertSame(Tag.HEAD, head.tag());
        Element body = head.getNextElementSibling();
        assertSame(Tag.BODY, body.tag());
    }

    /**
     * Test the head method.
     */
    @Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNull(head);

        document.title("Test Title");
        head = document.head();
        Element title = head.getFirstElementChild();
        assertEquals("Test Title", title.text());
    }

    /**
     * Test the body method.
     */
    @Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNull(body);
    }

    /**
     * Test the title method.
     */
    @Test
    public void testTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title(null);
        assertNull(document.title());
    }

    /**
     * Test the title method with an empty string.
     */
    @Test
    public void testTitleEmptyString() {
        Document document = new Document("baseUri");
        document.title("");
        assertEquals("", document.title());
    }

    /**
     * Test the createElement method.
     */
    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    /**
     * Test the normalise method.
     */
    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.appendElement(new TextNode("Text before"));
        document.appendElement(new TextNode("Text within body"));
        document.appendElement(new TextNode("Text after"));
        document.appendElement(new Element("html"));
        document.appendElement(new Element("head"));
        document.appendElement(new Element("head")); // duplicate
        document.appendElement(new Element("body"));
        document.appendElement(new Element("body")); // duplicate

        document.normalise();

        assertEquals(1, document.childNodes().size());
        assertTrue(document.childNodes().get(0) instanceof Element);
        Element element = (Element) document.childNodes().get(0);
        assertEquals(1, element.childNodes().size());
        assertEquals("Text within body", element.childNodes().get(0).text());
    }

    /**
     * Test the findFirstElementByTagName method.
     */
    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = document.findFirstElementByTagName("title", this);
        assertNull(title);

        title = document.findFirstElementByTagName("title", head);
        assertSame(head, title.parent());
    }

    /**
     * Test the outerHtml method.
     */
    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", document.outerHtml());
    }

    /**
     * Test the text method.
     */
    @Test
    public void testText() {
        Document document = new Document("baseUri");
        document.body().text("Test Text");
        assertEquals("Test Text", document.text());
    }

    /**
     * Test the nodeName method.
     */
    @Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    /**
     * Test the clone method.
     */
    @Test
    public void testClone() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        Document clonedDocument = document.clone();
        assertEquals("Test Title", clonedDocument.title());
    }

    /**
     * Test the OutputSettings class.
     */
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(Document.OutputSettings.Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        Document.OutputSettings clonedOutputSettings = outputSettings.clone();
        assertEquals(Document.OutputSettings.Entities.EscapeMode.base, clonedOutputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, clonedOutputSettings.charset());
        assertEquals(true, clonedOutputSettings.prettyPrint());
        assertEquals(1, clonedOutputSettings.indentAmount());
    }
}