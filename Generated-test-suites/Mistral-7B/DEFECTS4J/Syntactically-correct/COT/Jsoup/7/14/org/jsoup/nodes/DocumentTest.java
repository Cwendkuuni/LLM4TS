package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @org.junit.Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(head);
        assertNotNull(body);
    }

    @org.junit.Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        assertEquals(document, head.parent());
    }

    @org.junit.Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals(document, body.parent());
    }

    @org.junit.Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());
    }

    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.head().appendElement("p").text("Test Head");
        document.body().appendElement("p").text("Test Body");
        document.body().appendElement("p").text("Test Body 2");
        document.normalise();
        List<Element> heads = document.getElementsByTag("head");
        List<Element> bodies = document.getElementsByTag("body").childNodes();
        assertEquals(1, heads.size());
        assertEquals(2, bodies.size());
        Element firstBodyElement = (Element) bodies.get(0);
        assertTrue(firstBodyElement.text().isEmpty());
        Element secondBodyElement = (Element) bodies.get(1);
        assertEquals("Test Body", secondBodyElement.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTitleMethodWithNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTitleMethodWithEmptyTitle() {
        Document document = new Document("baseUri");
        document.title("");
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element div = document.findFirstElementByTagName("div", this);
        assertNull(div);
        Element bodyDiv = body.findFirstElementByTagName("div", this);
        assertNull(bodyDiv);
        Element titleDiv = title.findFirstElementByTagName("div", this);
        assertNull(titleDiv);
        Element headDiv = head.findFirstElementByTagName("div", this);
        assertNull(headDiv);
        Element htmlDiv = html.findFirstElementByTagName("div", this);
        assertEquals("div", htmlDiv.nodeName());
        assertEquals(html, htmlDiv.parent());
    }

    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element div = document.createElement("div");
        div.text("Test Div");
        body.appendChild(div);
        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head><title>Test Title</title></head><body><div>Test Div</div></body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Text");
        String text = document.text();
        assertEquals("Test Text", text);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTextMethodWithNullText() {
        Document document = new Document("baseUri");
        document.text(null);
    }

    @org.junit.Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testOutputSettingsClass() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}