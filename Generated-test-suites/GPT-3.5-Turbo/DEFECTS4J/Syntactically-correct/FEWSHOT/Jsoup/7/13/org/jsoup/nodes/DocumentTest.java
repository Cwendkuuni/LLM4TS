package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.head().getElementsByTag("title").first());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.getElementsByTag("title").first().remove();
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleElementNotFound() {
        Document doc = new Document("http://example.com");
        doc.getElementsByTag("head").remove();
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("head");
        doc.appendElement("body");

        doc.normalise();

        assertNotNull(doc.head().parent());
        assertNotNull(doc.body().parent());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        doc.appendText("Test Text");
        doc.appendElement("div");

        doc.normalise();

        assertEquals("Test Text", doc.body().text());
        assertNull(doc.body().previousSibling());
        assertEquals(" ", doc.body().nextSibling().outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("div");

        assertEquals("<div></div>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("div");

        doc.text("Test Text");

        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testEscapeMode() {
        Document doc = new Document("http://example.com");
        assertEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());

        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
    }

    @Test
    public void testCharset() {
        Document doc = new Document("http://example.com");
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());

        doc.outputSettings().charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
    }

    @Test
    public void testCharsetString() {
        Document doc = new Document("http://example.com");
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());

        doc.outputSettings().charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
    }

    @Test
    public void testPrettyPrint() {
        Document doc = new Document("http://example.com");
        assertEquals(true, doc.outputSettings().prettyPrint());

        doc.outputSettings().prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document doc = new Document("http://example.com");
        assertEquals(1, doc.outputSettings().indentAmount());

        doc.outputSettings().indentAmount(4);
        assertEquals(4, doc.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.outputSettings());
    }

}