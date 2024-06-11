package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        Element html = doc.appendElement("html");
        assertNotNull(html);
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
        Element html = doc.appendElement("html");
        assertNotNull(html);
        body = doc.body();
        assertNull(body);
        Element bodyElement = html.appendElement("body");
        assertNotNull(bodyElement);
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
        Element html = doc.appendElement("html");
        assertNotNull(html);
        Element head = doc.head();
        assertNotNull(head);
        Element titleElement = head.appendElement("title");
        assertNotNull(titleElement);
        titleElement.text("Example Title");
        title = doc.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        assertNotNull(html);
        Element head = doc.head();
        assertNotNull(head);
        doc.title("New Title");
        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = doc.head();
        assertNotNull(head);
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        assertNotNull(html);
        Element head = html.appendElement("head");
        assertNotNull(head);
        Element body = html.appendElement("body");
        assertNotNull(body);
        TextNode textNode1 = new TextNode("hello", "");
        TextNode textNode2 = new TextNode("world", "");
        head.appendChild(textNode1);
        body.appendChild(textNode2);
        doc.normaliseTextNodes(doc);
        assertEquals(textNode1, body.childNode(0));
        assertEquals(textNode2, body.childNode(1));
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertSame(Document.OutputSettings.class, outputSettings.getClass());
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        Charset charset = Charset.forName("ISO-8859-1");
        boolean prettyPrint = false;
        int indentAmount = 4;
        
        outputSettings.escapeMode(escapeMode);
        outputSettings.charset(charset);
        outputSettings.prettyPrint(prettyPrint);
        outputSettings.indentAmount(indentAmount);

        assertEquals(escapeMode, outputSettings.escapeMode());
        assertEquals(charset, outputSettings.charset());
        assertEquals(prettyPrint, outputSettings.prettyPrint());
        assertEquals(indentAmount, outputSettings.indentAmount());
    }

    // Additional test cases can be added for thorough testing of all methods in the Document class
}