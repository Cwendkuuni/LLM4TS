package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.tagName());
        Assert.assertEquals("baseUri", document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Assert.assertNotNull(document);
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Element body = html.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        document.head();
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        document.body();
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleMethodGet() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleMethodSet() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String oldTitle = document.title();
        document.title("New Test Title");
        String newTitle = document.title();
        Assert.assertEquals("New Test Title", newTitle);
        Assert.assertNotEquals(oldTitle, newTitle);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodSetNull() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        Assert.assertNotNull(element);
        Assert.assertEquals("p", element.tagName());
        Assert.assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Text");
        document.body().appendElement("p").text("Another Test Text");
        document.normalise();
        Element title = document.head().getElementsByTag("title").first();
        List<Node> bodyChildren = document.body().childNodes();
        Assert.assertEquals(1, title.childNodes().size());
        Assert.assertEquals(3, bodyChildren.size());
        TextNode firstTextNode = (TextNode) bodyChildren.get(0);
        TextNode secondTextNode = (TextNode) bodyChildren.get(1);
        String firstText = firstTextNode.text();
        String secondText = secondTextNode.text();
        Assert.assertEquals("Test Text", firstText.trim());
        Assert.assertEquals("Another Test Text", secondText.trim());
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = html.appendElement("title");
        Element p = body.appendElement("p");

        Element foundElement = document.findFirstElementByTagName("html", this);
        Assert.assertEquals(html, foundElement);

        foundElement = document.findFirstElementByTagName("title", this);
        Assert.assertEquals(title, foundElement);

        foundElement = document.findFirstElementByTagName("p", this);
        Assert.assertEquals(p, foundElement);

        foundElement = document.findFirstElementByTagName("body", this);
        Assert.assertEquals(body, foundElement);

        foundElement = document.findFirstElementByTagName("head", null);
        Assert.assertNull(foundElement);

        foundElement = document.findFirstElementByTagName("html", null);
        Assert.assertNull(foundElement);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Text");

        String output = document.outerHtml();
        String expectedOutput = "<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.body().appendElement("p").text("Test Text");

        String text = document.text();
        Assert.assertEquals("Test Text", text);
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethodNullText() {
        Document document = new Document("baseUri");
        document.body().appendElement("p").text("Test Text");
        document.text(null);
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsAccessor() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertNotNull(outputSettings.encoder());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        Assert.assertSame(outputSettings, document.outputSettings());
    }
}