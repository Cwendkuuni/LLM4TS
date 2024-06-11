package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.s;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.title());
        Element html = document.head();
        assertNull(html);
        html = document.body();
        assertNull(html);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        assertNotNull(html);
        Element head = html.child(0);
        Element title = head.child(0);
        assertEquals("title", title.tagName());
        assertEquals("", title.text());
        html = document.body();
        assertNotNull(html);
        Element body = html.child(0);
        assertEquals("body", body.tagName());
        assertEquals("", body.text());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        Element titleEl = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element link = document.createElement("a");
        assertEquals("a", link.tagName());
        assertEquals(baseUri, link.baseUri());
        assertNull(link.text());
        document.appendElement(link);
        Element parent = document.lastChild();
        assertEquals(parent, link.parent());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Test Text 1");
        TextNode textNode2 = body.appendText("Test Text 2 ");
        document.normalise();
        Element titleEl = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text());
        Element bodyEl = document.getElementsByTag("body").first();
        Element textNode1El = bodyEl.getElementsByText("Test Text 1").first();
        assertEquals(textNode1, textNode1El.parent());
        Element textNode2El = bodyEl.getElementsByText("Test Text 2 ").first();
        assertEquals(textNode2, textNode2El.parent());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Test Text 1");
        TextNode textNode2 = body.appendText("Test Text 2 ");
        document.normalise();
        Element titleEl = document.findFirstElementByTagName("title", this);
        assertEquals("title", titleEl.tagName());
        assertEquals("Test Title", titleEl.text());
        Element bodyEl = document.findFirstElementByTagName("body", this);
        assertEquals("body", bodyEl.tagName());
        TextNode textNode1El = document.findFirstElementByTagName("text", bodyEl);
        assertNull(textNode1El);
        TextNode textNode2El = document.findFirstElementByTagName("Text", bodyEl);
        assertNull(textNode2El);
        TextNode textNode1El2 = document.findFirstElementByTagName("text", bodyEl);
        assertEquals(textNode1, textNode1El2);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Test Text 1");
        TextNode textNode2 = body.appendText("Test Text 2 ");
        document.normalise();
        String outerHtml = document.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body>" +
                "<text node=\"1\">Test Text 1</text>" +
                "<text node=\"2\">Test Text 2 </text>" +
                "</body></html>", outerHtml);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        body.text("Test Text");
        assertEquals("Test Text", document.text());
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Test Text 1");
        TextNode textNode2 = body.appendText("Test Text 2 ");
        document.normalise();
        Document clonedDocument = document.clone();
        assertEquals("#document", clonedDocument.nodeName());
        assertEquals(baseUri, clonedDocument.baseUri());
        assertEquals("Test Title", clonedDocument.title());
        Element clonedHtml = clonedDocument.head();
        Element clonedHead = clonedHtml.child(0);
        Element clonedTitle = clonedHead.child(0);
        assertEquals("title", clonedTitle.tagName());
        assertEquals("Test Title", clonedTitle.text());
        Element clonedBody = clonedHtml.child(1);
        TextNode clonedTextNode1 = clonedBody.child(0);
        TextNode clonedTextNode2 = clonedBody.child(1);
        assertEquals("text", clonedTextNode1.tagName());
        assertEquals("Test Text 1", clonedTextNode1.text());
        assertEquals("text", clonedTextNode2.tagName());
        assertEquals("Test Text 2 ", clonedTextNode2.text());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document();
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertFalse(outputSettings.prettyPrint());
        outputSettings.prettyPrint(true);
        assertTrue(outputSettings.prettyPrint());
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }
}