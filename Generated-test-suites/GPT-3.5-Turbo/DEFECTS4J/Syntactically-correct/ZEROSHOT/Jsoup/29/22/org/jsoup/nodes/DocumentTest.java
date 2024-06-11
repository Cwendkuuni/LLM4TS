package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("https://www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNotNull(document.head().getElementsByTag("title"));
    }

    @Test
    public void testHead() {
        Document document = new Document("https://www.example.com");
        Element head = document.head();
        assertNull(head);

        Element headElement = new Element(Tag.valueOf("head"), document.baseUri());
        document.appendChild(headElement);

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("https://www.example.com");
        Element body = document.body();
        assertNull(body);

        Element bodyElement = new Element(Tag.valueOf("body"), document.baseUri());
        document.appendChild(bodyElement);

        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://www.example.com");
        String title = document.title();
        assertEquals("", title);

        Element titleElement = new Element(Tag.valueOf("title"), document.baseUri());
        titleElement.text("Example Title");
        document.head().appendChild(titleElement);

        title = document.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://www.example.com");
        document.normalise();
        assertTrue(document.getElementsByTag("head").size() > 0);
        assertTrue(document.getElementsByTag("body").size() > 0);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://www.example.com");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertTrue(outerHtml.startsWith("<html>"));
        assertTrue(outerHtml.endsWith("</html>"));
    }

    @Test
    public void testText() {
        Document document = new Document("https://www.example.com");
        assertEquals("", document.text());

        document.text("Example Text");
        assertEquals("Example Text", document.text());
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://www.example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = new Document("https://www.example.com");
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("https://www.example.com");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());

        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}