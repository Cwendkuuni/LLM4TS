package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialization code here, if needed
    }

    @org.junit.Test
    public void testDocumentConstruction() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @org.junit.Test
    public void testGetHeadAndBody() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        Element body = doc.body();
        assertEquals("#document", doc.nodeName());
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @org.junit.Test
    public void testGetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @org.junit.Test
    public void testSetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title("New Test Title");
        assertEquals("New Test Title", doc.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element newElement = doc.createElement("div");
        assertEquals("div", newElement.nodeName());
        assertEquals("baseUri", newElement.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        Element title = doc.head().appendElement("title").text("Test Title 2");
        Element body = doc.body();
        body.appendText("Test text 1 ");
        body.appendText("Test text 2");

        doc.normalise();

        assertEquals("Test Title", doc.title());
        Element foundTitle = doc.head().getElementsByTag("title").first();
        assertEquals("Test Title", foundTitle.text());

        Element bodyElement = doc.body();
        Element textNode1 = bodyElement.getFirstTextNode();
        Element textNode2 = bodyElement.getFirstTextNode().getNextSibling();
        assertEquals("Test text 1 ", textNode1.text());
        assertEquals("Test text 2", textNode2.text());
    }

    @org.junit.Test
    public void testNormaliseStructure() {
        Document doc = Document.createShell("baseUri");
        Element title1 = doc.head().appendElement("title").text("Test Title 1");
        Element title2 = doc.head().appendElement("title").text("Test Title 2");
        doc.normalise();

        Element masterTitle = doc.head().getElementsByTag("title").first();
        assertEquals(1, masterTitle.childNodes().size());

        Element head = doc.head();
        assertEquals(1, head.childNodes().size());
        Element body = doc.body();
        assertEquals(2, body.childNodes().size());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        html.appendElement("html");
        Element head = doc.head();
        head.appendElement("head");
        Element body = doc.body();
        body.appendElement("body");

        Element foundHtml = doc.findFirstElementByTagName("html", this);
        Element foundHead = doc.findFirstElementByTagName("head", this);
        Element foundBody = doc.findFirstElementByTagName("body", this);

        assertEquals(html, foundHtml);
        assertEquals(head, foundHead);
        assertEquals(body, foundBody);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document doc = Document.createShell("baseUri");
        Element node = null;
        doc.findFirstElementByTagName("html", node);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTag() {
        Document doc = Document.createShell("baseUri");
        doc.findFirstElementByTagName("invalidTag", this);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element title = doc.head().appendElement("title").text("Test Title");
        Element body = doc.body();
        body.appendText("Test text 1 ");
        body.appendText("Test text 2");

        doc.normalise();

        String outerHtml = doc.outerHtml();
        String expectedOuterHtml = "<html><head><title>Test Title</title></head><body>Test text 1 Test text 2</body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @org.junit.Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        Element body = doc.body();
        body.text("Test text 1 Test text 2");

        assertEquals("Test text 1 Test text 2", doc.text());
    }

    @org.junit.Test
    public void testNodeName() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @org.junit.Test
    public void testClone() {
        Document doc = Document.createShell("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        Document clonedDoc = doc.clone();
        assertEquals(outputSettings, clonedDoc.outputSettings());
    }
}