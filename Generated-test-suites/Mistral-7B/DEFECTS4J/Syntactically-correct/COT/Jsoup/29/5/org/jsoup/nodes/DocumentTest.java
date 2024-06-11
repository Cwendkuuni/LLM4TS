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

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertTrue(doc.head() == null);
        assertTrue(doc.body() == null);
    }

    @org.junit.Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @org.junit.Test
    public void testHeadAccessors() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);
        doc.head = new Element(Tag.HEAD, doc);
        head = doc.head();
        assertNotNull(head);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @org.junit.Test
    public void testBodyAccessors() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        doc.body = new Element(Tag.BODY, doc);
        body = doc.body();
        assertNotNull(body);
        doc.text("Test Body");
        assertEquals("Test Body", body.text());
    }

    @org.junit.Test
    public void testTitleAccessors() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.nodeName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title", "Test Title");
        Element body = doc.appendElement("body");
        doc.text(body, "Test Body");
        TextNode textNode = doc.appendTextNode("Test Text");

        doc.normalise();

        assertEquals(6, doc.childNodes().size());
        assertEquals(1, html.childNodes().size());
        assertEquals(3, html.get(0).childNodes().size());
        assertEquals(1, head.childNodes().size());
        assertEquals(1, title.childNodes().size());
        assertEquals(1, body.childNodes().size());
        assertEquals(1, textNode.parent().childNodes().size());

        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("title", title.nodeName());
        assertEquals("body", body.nodeName());

        assertEquals("Test Title", title.text());
        assertEquals("Test Body", body.text());
        assertEquals("Test Text", textNode.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document doc = new Document("baseUri");
        normaliseTextNodes(null);
    }

    private void normaliseTextNodes(Element element) {
        // Test implementation here
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title", "Test Title");

        Element found = doc.findFirstElementByTagName("html", this);
        assertEquals(html, found);

        found = doc.findFirstElementByTagName("title", this);
        assertEquals(title, found);

        found = doc.findFirstElementByTagName("unknown", this);
        assertEquals(null, found);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title", "Test Title");
        Element body = doc.appendElement("body");
        doc.text(body, "Test Body");

        String htmlString = doc.outerHtml();
        String expectedHtmlString = "<html><head></head><body>" +
                "Test Body</body></html>";

        assertEquals(expectedHtmlString, htmlString);
    }

    @org.junit.Test
    public void testText() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        doc.text(body, "Test Body");

        String bodyText = doc.text();
        String expectedBodyText = "Test Body";

        assertEquals(expectedBodyText, bodyText);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTextNullBody() {
        Document doc = new Document("baseUri");
        doc.text(null, "Test Body");
    }

    @org.junit.Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @org.junit.Test
    public void testClone() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);
        Document clonedDoc = doc.clone();

        assertEquals(doc, clonedDoc);
        assertNotSame(doc, clonedDoc);
        assertNotSame(outputSettings, clonedDoc.outputSettings());
    }

    @org.junit.Test
    public void testOutputSettingsEscapeMode() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());

        doc.outputSettings(outputSettings.escapeMode(Entities.EscapeMode.extended));
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
    }

    @org.junit.Test
    public void testOutputSettingsCharset() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals("UTF-8", outputSettings.charset().name());

        doc.outputSettings(outputSettings.charset("ISO-8859-1"));
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }

    @org.junit.Test
    public void testOutputSettingsPrettyPrint() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        assertTrue(outputSettings.prettyPrint());

        doc.outputSettings(outputSettings.prettyPrint(false));
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @org.junit.Test
    public void testOutputSettingsIndentAmount() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(1, outputSettings.indentAmount());

        doc.outputSettings(outputSettings.indentAmount(2));
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}