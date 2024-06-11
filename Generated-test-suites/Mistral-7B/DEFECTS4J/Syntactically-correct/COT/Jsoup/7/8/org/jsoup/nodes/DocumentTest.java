package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * DocumentTest class for testing Document class methods.
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any necessary setup here
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
    }

    /**
     * Test createShell method.
     */
    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test head method.
     */
    @org.junit.Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    /**
     * Test body method.
     */
    @org.junit.Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test title method.
     */
    @org.junit.Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    /**
     * Test title method with existing title.
     */
    @org.junit.Test
    public void testTitleMethodWithExistingTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    /**
     * Test createElement method.
     */
    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    /**
     * Test normalise method with empty document.
     */
    @org.junit.Test
    public void testNormaliseMethodWithEmptyDocument() {
        Document document = new Document("baseUri");
        document.normalise();
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test normalise method with text nodes in root.
     */
    @org.junit.Test
    public void testNormaliseMethodWithTextNodesInRoot() {
        Document document = new Document("baseUri");
        document.appendText("Test Text 1");
        document.appendText("Test Text 2");
        document.normalise();
        Element body = document.body();
        TextNode textNode1 = (TextNode) body.child(0);
        TextNode textNode2 = (TextNode) body.child(1);
        assertEquals("Test Text 1", textNode1.text());
        assertEquals("Test Text 2", textNode2.text());
    }

    /**
     * Test findFirstElementByTagName method.
     */
    @org.junit.Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.appendElement("title");
        Element title2 = document.appendElement("title");
        Element element = document.findFirstElementByTagName("title", body);
        assertNull(element);
        element = document.findFirstElementByTagName("title", html);
        assertEquals("title", element.tagName());
        element = document.findFirstElementByTagName("title", head);
        assertEquals("title", element.tagName());
        List<Element> titles = document.getElementsByTag("title");
        assertEquals(2, titles.size());
        Element firstTitle = titles.get(0);
        assertEquals("title", firstTitle.tagName());
        assertEquals("title", titles.get(1).tagName());
    }

    /**
     * Test outerHtml method.
     */
    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        document.title("Test Title");
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.contains("<html>"));
        assertTrue(outerHtml.contains("<head>"));
        assertTrue(outerHtml.contains("<title>Test Title</title>"));
        assertTrue(outerHtml.contains("<body>"));
        assertEquals(outerHtml, document.html());
    }

    /**
     * Test text method.
     */
    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Text 1 Test Text 2");
        String text = document.text();
        assertEquals("Test Text 1 Test Text 2", text);
        document.text("Test Text 3 Test Text 4");
        text = document.text();
        assertEquals("Test Text 3 Test Text 4", text);
    }

    /**
     * Test nodeName method.
     */
    @org.junit.Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    /**
     * Test OutputSettings class methods.
     */
    @org.junit.Test
    public void testOutputSettingsClassMethods() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        outputSettings.indentAmount(5);
        assertEquals(5, outputSettings.indentAmount());
    }

    /**
     * Test edge cases and exception handling.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        Document document = new Document(null);
    }

    /**
     * Test edge cases and exception handling.
     */
    @org.junit.Test
    public void testEdgeCases() {
        Document document = new Document("baseUri");
        document.title(null);
        assertEquals("", document.title());
        Element emptyTitle = document.appendElement("title");
        document.title(emptyTitle);
        assertEquals("", document.title());
        document.title("");
        assertEquals("", document.title());
        document.title("Test Title\u0000");
        assertEquals("Test Title", document.title());
        document.title("Test Title\uD83C\uDFFB\uD83C\uDFFF");
        assertEquals("Test Title", document.title());
        document.title("\uD83C\uDFFB\uD83C\uDFFF Test Title");
        assertEquals("\uD83C\uDFFB\uD83C\uDFFF Test Title", document.title());
    }
}