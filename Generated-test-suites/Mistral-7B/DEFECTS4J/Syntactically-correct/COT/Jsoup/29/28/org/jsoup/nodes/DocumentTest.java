package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        Element html = document.findFirstElementByTagName("html");
        assertNotNull(html);
        Element head = html.getFirstElementByTagName("head");
        assertNotNull(head);
        Element body = html.getFirstElementByTagName("body");
        assertNotNull(body);
    }

    @Test
    public void testHeadAccessors() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();
        assertNull(head);
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
        head = document.head();
        Element titleElement = head.getFirstElementByTagName("title");
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text().trim());
    }

    @Test
    public void testBodyAccessors() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        assertNull(body);
        document.text("Test Body");
        assertEquals("Test Body", document.body().text().trim());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.outerHtml("<html><head><title>Test Title</title></head><body><div>Test</div></body></html>");
        document.normalise();
        Element body = document.body();
        assertEquals(1, body.childNodes().size());
        Element div = body.getFirstChild();
        assertEquals("Test", div.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document document = new Document("");
        document.normalise(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.outerHtml("<html><head><title>Test Title</title></head><body><div id=\"test\"></div></body></html>");
        document.normalise();
        Element html = document.findFirstElementByTagName("html", document);
        Element div = document.findFirstElementByTagName("div", document);
        Element title = document.findFirstElementByTagName("title", document);
        assertEquals(html, document);
        assertEquals(div, document.body().getFirstElementByTagName("div"));
        assertEquals(title, document.head().getFirstElementByTagName("title"));
        assertNull(document.findFirstElementByTagName("table", document));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.outerHtml("<html><head><title>Test Title</title></head><body><div>Test</div></body></html>");
        assertEquals("<html><head><title>Test Title</title></head><body><div>Test</div></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.text("Test Text");
        assertEquals("Test Text", document.text().trim());
    }

    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.outerHtml("<html><head><title>Test Title</title></head><body><div>Test</div></body></html>");
        Document clone = document.clone();
        assertEquals("#document", clone.nodeName());
        assertEquals(baseUri, clone.baseUri());
        assertEquals(document.outputSettings(), clone.outputSettings());
        assertEquals(document.quirksMode(), clone.quirksMode());
    }

    @Test
    public void testOutputSettingsGetters() {
        Charset charset = Charset.forName("UTF-8");
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.charset(charset);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(charset, outputSettings.charset());
        assertEquals(charset, outputSettings.encoder().encoding());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsSetters() {
        Charset charset = Charset.forName("UTF-8");
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        outputSettings.charset(charset);
        assertEquals(charset, outputSettings.charset());
        assertEquals(charset, outputSettings.encoder().encoding());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsClone() {
        Charset charset = Charset.forName("UTF-8");
        OutputSettings original = new OutputSettings();
        original.charset(charset);
        OutputSettings clone = original.clone();
        assertEquals(charset, clone.charset());
        assertEquals(Entities.EscapeMode.base, clone.escapeMode());
        assertFalse(clone.prettyPrint());
        assertEquals(1, clone.indentAmount());
    }
}