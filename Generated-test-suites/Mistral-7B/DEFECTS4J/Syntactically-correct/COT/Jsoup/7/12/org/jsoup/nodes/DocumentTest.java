package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstruction() {
        Document doc = new Document("baseUri");
        Assert.assertNotNull(doc);
        Assert.assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());

        html = doc.body();
        Assert.assertNotNull(html);
        Assert.assertEquals("body", html.tagName());
    }

    @Test
    public void testHeadAccess() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyAccess() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleAccess() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSettingNull() {
        Document doc = new Document("baseUri");
        try {
            doc.title(null);
            fail("Should have thrown a null pointer exception.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element p = doc.createElement("p");
        Assert.assertNotNull(p);
        Assert.assertEquals("p", p.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        body.appendText("Text to be moved.");
        doc.normalise();
        Node textNode = body.childNodes().get(0);
        Assert.assertTrue(textNode instanceof TextNode);
        Assert.assertEquals("Text to be moved.", textNode.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element p = body.appendElement("p");

        Element found = doc.findFirstElementByTagName("html", doc);
        Assert.assertEquals(html, found);

        found = doc.findFirstElementByTagName("body", doc);
        Assert.assertEquals(body, found);

        found = doc.findFirstElementByTagName("p", body);
        Assert.assertEquals(p, found);

        found = doc.findFirstElementByTagName("script", doc);
        Assert.assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        body.appendText("Test");

        String htmlString = doc.outerHtml();
        String expectedHtmlString = "<html><head></head><body>Test</body></html>";
        Assert.assertEquals(expectedHtmlString, htmlString);
    }

    @Test
    public void testTextSettingNull() {
        Document doc = new Document("baseUri");
        try {
            doc.text(null);
            fail("Should have thrown a null pointer exception.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testsetText() {
        Document doc = new Document("baseUri");
        doc.body().text("Test");
        String text = doc.body().text();
        Assert.assertEquals("Test", text);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        Assert.assertEquals("UTF-8", settings.charset().name());
        Assert.assertTrue(settings.prettyPrint());
        Assert.assertEquals(1, settings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();
        settings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();
        Charset charset = Charset.forName("ISO-8859-1");
        settings.charset(charset);
        Assert.assertEquals(charset, settings.charset());
        Assert.assertEquals("ISO-8859-1", settings.encoder().encode().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();
        settings.prettyPrint(false);
        Assert.assertFalse(settings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document doc = new Document("baseUri");
        OutputSettings settings = doc.outputSettings();
        settings.indentAmount(4);
        Assert.assertEquals(4, settings.indentAmount());
    }
}