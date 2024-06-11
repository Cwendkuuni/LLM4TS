package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleMethodWithExistingTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");

        String title = document.title();
        assertEquals("Test Title", title);

        document.title("New Test Title");
        title = document.title();
        assertEquals("New Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.body().appendText("Test 1");
        document.appendText("Test 2");
        document.appendElement("html").appendText("Test 3");

        document.normalise();

        Element body = document.body();
        TextNode textNode1 = (TextNode) body.childNodes().get(0);
        assertEquals("Test 1", textNode1.text());
        TextNode textNode2 = (TextNode) body.childNodes().get(1);
        assertEquals("Test 2", textNode2.text());
        assertEquals(0, document.getElementsByTag("html").size());
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element tagElement = document.findFirstElementByTagName("html", document);
        assertEquals(html, tagElement);

        tagElement = document.findFirstElementByTagName("head", document);
        assertEquals(head, tagElement);

        tagElement = document.findFirstElementByTagName("body", document);
        assertEquals(body, tagElement);

        tagElement = document.findFirstElementByTagName("unknown", document);
        assertNull(tagElement);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        head.appendText("Test Head");
        body.appendText("Test Body");

        assertEquals("<html><head>Test Head</head><body>Test Body</body></html>", document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();

        body.text("Test Text");

        assertEquals("Test Text", document.text());
    }

    @Test
    public void testOutputSettingsAccessors() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings.charset("UTF-16");
        assertEquals("UTF-16", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings.indentAmount(5);
        assertEquals(5, outputSettings.indentAmount());
    }
}