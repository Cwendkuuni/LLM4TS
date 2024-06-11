package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.tagName());
    }

    @org.junit.Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test
    public void testTitleSettingNull() {
        Document document = new Document("baseUri");
        try {
            document.title(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testTitleSettingEmptyString() {
        Document document = new Document("baseUri");
        document.title("");
        String title = document.title();
        assertEquals("", title);
    }

    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.body().appendText("Test");
        document.appendText(" Test ");
        document.appendElement(new TextNode(" Another test ", ""));
        document.appendElement(new Element(Tag.valueOf("p"), ""));
        document.appendElement(new Element(Tag.valueOf("p"), ""));

        document.normalise();

        List<Node> bodyChildren = document.body().children();
        assertEquals(6, bodyChildren.size());
        assertEquals("Test Test ", bodyChildren.get(0).text());
        assertEquals(" Another test ", bodyChildren.get(1).text());
        assertEquals("", bodyChildren.get(2).text());
        assertEquals("", bodyChildren.get(3).tagName());
        assertEquals("", bodyChildren.get(4).tagName());
        assertEquals("", bodyChildren.get(5).tagName());
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element body = html.appendElement("body");
        Element head = html.appendElement("head");
        Element title = body.appendElement("title");

        Element found = document.findFirstElementByTagName("head", document);
        assertEquals(head, found);

        found = document.findFirstElementByTagName("body", null);
        assertEquals(body, found);

        found = document.findFirstElementByTagName("html", null);
        assertEquals(html, found);

        found = document.findFirstElementByTagName("title", null);
        assertEquals(title, found);

        found = document.findFirstElementByTagName("title", document);
        assertEquals(title, found);

        found = document.findFirstElementByTagName("nonexistent", document);
        assertNull(found);
    }

    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");

        String htmlString = document.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body></body></html>";
        assertEquals(expected, htmlString);
    }

    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test");

        String text = document.text();
        assertEquals("Test", text);

        body.text("");
        text = document.text();
        assertEquals("", text);
    }

    @org.junit.Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testOutputSettingsGetters() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertNotNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}