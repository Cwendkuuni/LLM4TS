package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.getElementsByTag("html").first().nodeName());
        assertEquals("head", doc.getElementsByTag("head").first().nodeName());
        assertEquals("body", doc.getElementsByTag("body").first().nodeName());
        assertNull(doc.getElementsByTag("html").first().parentNode());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        
        Element titleElement = document.appendElement("title");
        titleElement.text("Test Title");
        assertEquals("Test Title", document.title());
        
        document.normalise();
        assertEquals("Test Title", document.title());
        
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testEqualsAndHashCode() {
        Document doc1 = new Document("http://example.com");
        Document doc2 = new Document("http://example.com");
        assertNotSame(doc1, doc2);
        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());

        Document doc3 = new Document("http://example.org");
        assertNotEquals(doc1, doc3);
        assertNotEquals(doc1.hashCode(), doc3.hashCode());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = document.outputSettings();
        assertNotNull(settings);
        assertTrue(settings.prettyPrint());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
        
        Document doc = document.outputSettings(settings);
        assertSame(settings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element div = body.appendElement("div");
        div.text("Hello, World!");

        Element title = head.appendElement("title");
        title.text("Test Title");

        assertNull(document.getElementById("div"));
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.getElementsByTag("title").first());

        document.normalise();

        assertEquals("Hello, World!", document.getElementsByTag("div").first().text());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNotNull(document.getElementsByTag("title").first());
    }
}