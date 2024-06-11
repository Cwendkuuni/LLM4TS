package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @org.junit.Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertEquals(document, head.parent());
    }

    @org.junit.Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(document, body.parent());
    }

    @org.junit.Test
    public void testTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTitleNullInput() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.text("Test text in root");
        document.title("Test Title");
        document.head().text("Test head text");
        document.appendElement("p").text("Test paragraph text");

        document.normalise();

        TextNode rootTextNode = (TextNode) document.childNodes().get(0);
        assertEquals("Test text in body", rootTextNode.text());
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text().trim());
        Element bodyElement = document.getElementsByTag("body").first();
        Element pElement = bodyElement.getElementsByTag("p").first();
        assertEquals("Test paragraph text", pElement.text());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element htmlElement = document.findFirstElementByTagName("html", document);
        assertNotNull(htmlElement);
        assertEquals("html", htmlElement.tagName());

        Element headElement = document.findFirstElementByTagName("head", document);
        assertNotNull(headElement);
        assertEquals("head", headElement.tagName());

        Element bodyElement = document.findFirstElementByTagName("body", document);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());

        Element nonExistentElement = document.findFirstElementByTagName("nonExistentTag", document);
        assertNull(nonExistentElement);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        document.text("Test text in root");
        document.title("Test Title");
        document.head().text("Test head text");
        document.appendElement("p").text("Test paragraph text");

        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head></head><body><title>Test Title</title>Test text in body</body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        document.text("Test text in body");

        String text = document.text();
        assertEquals("Test text in body", text);
    }

    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), newOutputSettings.charset());
        assertTrue(newOutputSettings.prettyPrint());
        assertEquals(1, newOutputSettings.indentAmount());
    }
}