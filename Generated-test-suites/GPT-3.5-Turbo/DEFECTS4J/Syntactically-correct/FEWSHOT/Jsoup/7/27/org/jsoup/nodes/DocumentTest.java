package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://www.example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        assertEquals("html", document.head().parent().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("https://www.example.com");
        assertNull(document.head());
        
        Element headElement = document.appendElement("head");
        assertEquals(headElement, document.head());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("https://www.example.com");
        assertNull(document.body());
        
        Element bodyElement = document.appendElement("body");
        assertEquals(bodyElement, document.body());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://www.example.com");
        assertNull(document.title());

        document.title("Page Title");
        assertEquals("Page Title", document.title());
    }

    @Test
    public void testStartElement() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://www.example.com");
        document.normalise();
        assertEquals("html", document.head().parent().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("https://www.example.com");
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("https://www.example.com");
        document.text("Hello World");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}