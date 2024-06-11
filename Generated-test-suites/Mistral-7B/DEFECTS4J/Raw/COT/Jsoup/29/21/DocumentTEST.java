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
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals("baseUri", doc.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertNotNull(html);
        assertEquals(Tag.HTML, html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals(Tag.HEAD, head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals(Tag.BODY, body.tagName());
    }

    @org.junit.Test
    public void testHeadAccessor() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals(Tag.HEAD, head.tagName());

        Element newHead = doc.createElement("head");
        doc.head(newHead);
        assertEquals(newHead, doc.head());
    }

    @org.junit.Test
    public void testBodyAccessor() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);

        Element newBody = doc.createElement("body");
        doc.body(newBody);
        assertEquals(newBody, doc.body());
    }

    @org.junit.Test
    public void testTitleAccessor() {
        Document doc = new Document("baseUri");
        doc.title("testTitle");
        String title = doc.title();
        assertEquals("testTitle", title);

        doc.title("");
        title = doc.title();
        assertEquals("", title);
    }

    @org.junit.Test
    public void testTitleSetter() {
        Document doc = new Document("baseUri");
        doc.title("testTitle");
        String title = doc.title();
        assertEquals("testTitle", title);

        doc.title("newTestTitle");
        title = doc.title();
        assertEquals("newTestTitle", title);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element newElement = doc.createElement("testTag");
        assertNotNull(newElement);
        assertEquals("testTag", newElement.tagName());
        assertEquals("baseUri", newElement.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.body().appendText("testText1");
        doc.head().appendText("testText2");

        doc.normalise();

        Element body = doc.body();
        TextNode bodyText = (TextNode) body.childNodes().get(0);
        assertEquals("testText1 testText2", bodyText.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document doc = new Document("baseUri");
        doc.normalise(null);
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.findFirstElementByTagName("html", doc);
        assertNotNull(html);
        assertEquals(Tag.HTML, html.tagName());

        Element head = doc.findFirstElementByTagName("head", doc);
        assertNotNull(head);
        assertEquals(Tag.HEAD, head.tagName());

        Element body = doc.findFirstElementByTagName("body", doc);
        assertNotNull(body);
        assertEquals(Tag.BODY, body.tagName());

        Element nonExistent = doc.findFirstElementByTagName("nonexistent", doc);
        assertNull(nonExistent);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        doc.title("testTitle");
        body.appendText("testText");

        String htmlString = doc.outerHtml();
        String expectedHtmlString = "<html><head><title>testTitle</title></head><body>testText</body></html>";
        assertEquals(expectedHtmlString, htmlString);
    }

    @org.junit.Test
    public void testText() {
        Document doc = new Document("baseUri");
        Element body = doc.body();

        doc.text("testText");
        String bodyText = body.text();
        assertEquals("testText", bodyText);

        body.appendText(" appendedText");
        bodyText = body.text();
        assertEquals("testText appendedText", bodyText);
    }

    @org.junit.Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @org.junit.Test
    public void testClone() {
        Document doc = new Document("baseUri");
        Document clone = doc.clone();

        doc.title("testTitle");
        assertFalse(doc.equals(clone));
        assertEquals("testTitle", doc.title());
        assertEquals("", clone.title());
    }

    @org.junit.Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        doc.outputSettings(new OutputSettings());
        outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        doc.outputSettings(new OutputSettings().escapeMode(Entities.EscapeMode.extended));
        outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());

        doc.outputSettings(new OutputSettings().charset(Charset.forName("US-ASCII")));
        outputSettings = doc.outputSettings();
        assertEquals(Charset.forName("US-ASCII"), outputSettings.charset());

        doc.outputSettings(new OutputSettings().prettyPrint(false));
        outputSettings = doc.outputSettings();
        assertEquals(false, outputSettings.prettyPrint());

        doc.outputSettings(new OutputSettings().indentAmount(3));
        outputSettings = doc.outputSettings();
        assertEquals(3, outputSettings.indentAmount());
    }
}