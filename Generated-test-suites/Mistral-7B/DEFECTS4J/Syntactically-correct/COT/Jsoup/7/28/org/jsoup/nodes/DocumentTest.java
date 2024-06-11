package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Test class for Document.
 *
 * @author YourName
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.head();
        assertNotNull(html);
        Element head = html.head();
        assertNotNull(head);
        Element body = html.body();
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleMethod_EmptyTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleMethod_SetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementMethod_NullTagName() {
        Document document = new Document("baseUri");
        document.createElement(null);
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.head().text("Head Text");
        document.body().text("Body Text");
        document.text("Document Text");
        document.normalise();
        List<Element> heads = document.getElementsByTag("head");
        assertEquals(1, heads.size());
        List<Element> bodies = document.getElementsByTag("body");
        assertEquals(1, bodies.size());
        assertEquals("Head Text", heads.get(0).text());
        assertEquals("Body Text", bodies.get(0).text());
        assertEquals("Document Text", document.body().text());
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("html").appendElement("head");
        Element body = document.appendElement("html").appendElement("body");
        Element title = head.appendElement("title").text("Test Title");
        Element paragraph = body.appendElement("p").text("Test Paragraph");
        Element found = document.findFirstElementByTagName("title", document);
        assertNotNull(found);
        assertEquals("Test Title", found.text());
        found = document.findFirstElementByTagName("p", document);
        assertNotNull(found);
        assertEquals("Test Paragraph", found.text());
        found = document.findFirstElementByTagName("nonexistent", document);
        assertNull(found);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("html").appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        document.title("Document Title");
        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", document.outerHtml());
        assertEquals("<html><head><title>Document Title</title></head><body></body></html>", document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.body().text("Test Text");
        assertEquals("Test Text", document.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethod_NullText() {
        Document document = new Document("baseUri");
        document.text(null);
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsAccessors() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(2, outputSettings.encoder().flushBytes(new byte[1024]));
        assertFalse(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        OutputSettings newSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("UTF-16");
        assertEquals(Entities.EscapeMode.extended, newSettings.escapeMode());
        assertEquals("UTF-16", newSettings.charset().name());
        assertEquals(2, newSettings.encoder().flushBytes(new byte[1024]));
        assertTrue(newSettings.prettyPrint());
        assertEquals(2, newSettings.indentAmount());
    }
}