package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {
    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertEquals("head", html.nodeName());
        Element head = html.appendElement("title");
        assertEquals("title", head.nodeName());
        assertEquals("", head.text());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        Element body = doc.body();
        assertNull(body);

        Document shellDoc = Document.createShell("http://example.com");
        head = shellDoc.head();
        assertNotNull(head);
        body = shellDoc.body();
        assertNotNull(body);
    }

    @Test
    public void testAccessTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSettingTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

       doc.title("");
        assertEquals("", doc.title());

        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertEquals("a", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element title = doc.appendElement("title").text("Test Title");
        Element body = doc.appendElement("body").appendText("Test Body");
        Element head = doc.appendElement("head");
        Element script = doc.createElement("script").text("alert('Test');");
        head.appendElement("title").text("New Title");
        head.appendChild(script);

        doc.normalise();

        Element newTitle = doc.getElementsByTag("title").first();
        assertEquals("Test Title", newTitle.text());
        assertSame(title, newTitle);
        Element bodyElement = doc.body();
        assertEquals("Test Body", bodyElement.text());
        assertEquals(1, doc.head().childNodes().size());
        assertSame(script, doc.head().childNodes().get(0));
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element root = doc.appendElement("html").appendElement("body");
        root.appendText("Test 1").appendText("Test 2");

        doc.normalise();

        assertEquals(2, root.childNodes().size());
        TextNode textNode1 = (TextNode) root.childNodes().get(0);
        TextNode textNode2 = (TextNode) root.childNodes().get(1);
        assertEquals("Test 1", textNode1.text());
        assertEquals("Test 2", textNode2.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://example.com");
        Element root = doc.appendElement("html");
        Element head1 = root.appendElement("head");
        Element title1 = head1.appendElement("title").text("Title 1");
        Element body = root.appendElement("body");
        TextNode text1 = body.appendText("Text 1");
        Element head2 = doc.appendElement("head");
        TextNode text2 = head2.appendText("Text 2");

        doc.normalise();

        assertEquals(1, root.childNodes().size());
        Element head = root.childNodes().get(0);
        assertEquals(2, head.childNodes().size());
        assertEquals(1, head.getElementsByTag("title").size());
        Element title = head.getElementsByTag("title").first();
        assertEquals("Title 1", title.text());
        assertSame(title1, title);
        assertEquals(1, head.childNodes(1).childNodes().size());
        TextNode text = head.childNodes(1).childNodes().get(0);
        assertEquals("Text 2", text.text());
        assertSame(text2, text);
        assertEquals(1, body.childNodes().size());
        assertEquals("Text 1", body.childNodes().get(0).text());
        assertEquals(0, doc.getElementsByTag("head").size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element body = html.appendElement("body");
        Element head = html.appendElement("head");

        Element title = doc.findFirstElementByTagName("title", body);
        assertNull(title);
        title = doc.findFirstElementByTagName("title", head);
        assertNotNull(title);
        assertEquals("title", title.nodeName());
        assertEquals("", title.text());

        title.text("Test Title");
        assertEquals("Test Title", doc.findFirstElementByTagName("title", body).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body").appendText("Test Body");

        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body").appendText("Test Body");

        assertEquals("Test Body", doc.text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Element title = doc.appendElement("title").text("Test Title");
        Element body = doc.appendElement("body").appendText("Test Body");

        Document clonedDoc = doc.clone();
        assertEquals(2, clonedDoc.childNodes().size());
        Element titleCloned = clonedDoc.getElementsByTag("title").first();
        assertEquals("Test Title", titleCloned.text());
        assertSame(title, titleCloned);
        assertEquals("Test Body", clonedDoc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        Validate.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Validate.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Validate.assertEquals(true, outputSettings.prettyPrint());
        Validate.assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        Validate.assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        Validate.assertEquals(Charset.forName("UTF-8"), newOutputSettings.charset());
        Validate.assertEquals(true, newOutputSettings.prettyPrint());
        Validate.assertEquals(1, newOutputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        Validate.assertEquals(QuirksMode.noQuirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.quirks);
        Validate.assertEquals(QuirksMode.quirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.limitedQuirks);
        Validate.assertEquals(QuirksMode.limitedQuirks, doc.quirksMode());
    }
}