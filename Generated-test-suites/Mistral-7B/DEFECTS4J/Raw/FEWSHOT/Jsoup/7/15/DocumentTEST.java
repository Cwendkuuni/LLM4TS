package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.nodes.Document.OutputSettings.prettyPrint;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertEquals("#document", document.nodeName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals("<title>Test Title</title>", document.head().html());
    }

    @Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Jsoup.parse("<html><head>Test</head></html>", "http://example.com");
        document.normalise();
        List<Node> bodyChildren = document.body().childNodes();
        assertEquals(1, bodyChildren.size());
        Node node = bodyChildren.get(0);
        assertEquals("#text", node.nodeName());
        assertEquals("Test", node.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", "http://example.com");
        Element html = document.findFirstElementByTagName("html", document);
        Element head = document.findFirstElementByTagName("head", html);
        Element title = document.findFirstElementByTagName("title", head);
        Element body = document.findFirstElementByTagName("body", html);
        Element p = document.findFirstElementByTagName("p", body);

        assertEquals(html, title.parentElement());
        assertEquals(head, title.parentElement());
        assertEquals(body, p.parentElement());
        assertEquals(html, p.parentElement());
    }

    @Test
    public void testOutputSettings() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");

        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        assertEquals(prettyPrint, outputSettings.prettyPrint());
        assertEquals(0, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals(outputSettings, newOutputSettings);

        newOutputSettings = outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", newOutputSettings.charset().name());
        assertEquals(outputSettings, newOutputSettings);

        newOutputSettings = newOutputSettings.prettyPrint(false);
        assertFalse(newOutputSettings.prettyPrint());
        assertEquals(outputSettings, newOutputSettings);

        newOutputSettings = newOutputSettings.indentAmount(4);
        assertEquals(4, newOutputSettings.indentAmount());
        assertEquals(outputSettings, newOutputSettings);
    }
}