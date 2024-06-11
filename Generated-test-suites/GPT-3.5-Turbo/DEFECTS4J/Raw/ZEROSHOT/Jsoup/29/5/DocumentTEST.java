package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        assertNull(document.head());
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        assertNull(document.body());
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleNull() {
        Document document = new Document("http://example.com");
        assertThrows(IllegalArgumentException.class, () -> document.title(null));
    }

    @Test
    public void testTitleElementExists() {
        Document document = new Document("http://example.com");
        document.appendElement("head").appendElement("title").text("Existing Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleElementDoesNotExist() {
        Document document = new Document("http://example.com");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleElementParentIsNotHead() {
        Document document = new Document("http://example.com");
        document.appendElement("html").appendElement("title").text("Existing Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        body.appendChild(new TextNode("Some text"));
        document.normalise();
        assertEquals("Some text", body.text());
        assertEquals("", document.text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = new Document("http://example.com");
        Element element = document.appendElement("html");
        element.prependChild(new TextNode("Text 1"));
        element.appendChild(new Comment("Comment"));
        element.appendChild(new TextNode("Text 2"));
        document.normaliseTextNodes(element);
        assertEquals("Text 1 Text 2", element.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document document = new Document("http://example.com");
        Element htmlEl = document.appendElement("html");
        htmlEl.appendElement("head").text("Head");
        htmlEl.appendElement("body").text("Body");
        htmlEl.appendElement("head").text("Head 2");
        document.normaliseStructure("head", htmlEl);
        assertEquals(1, document.getElementsByTag("head").size());
        assertEquals("Head Head 2", document.head().text());
        assertEquals(2, document.head().childNodes().size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element element = document.appendElement("html");
        element.appendElement("head");
        element.appendElement("body");
        element.appendElement("div");
        assertEquals(document.head(), document.findFirstElementByTagName("head", document));
        assertEquals(document.body(), document.findFirstElementByTagName("body", document));
        assertEquals(element, document.findFirstElementByTagName("div", document));
        assertNull(document.findFirstElementByTagName("a", document));
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.appendElement("html").appendElement("head").text("Head");
        document.body().text("Body");
        assertEquals("<head>Head</head>\n<body>Body</body>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.body().appendElement("p").text("Test Paragraph");
        document.text("New Text");
        assertEquals("New Text", document.text());
        assertEquals("New Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = new Document("http://example.com");
        document.outputSettings().charset("UTF-16");
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
        assertNotSame(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        document.outputSettings(outputSettings);
        assertSame(outputSettings, document.outputSettings());
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}