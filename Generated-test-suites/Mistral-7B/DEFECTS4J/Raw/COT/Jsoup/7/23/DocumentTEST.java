package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertEquals("#root", document.tagName());
        assertSame(Tag.valueOf("#root"), document.getRootElement());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        assertEquals("html", html.tagName());
        assertNull(html.parent());
        assertSame(document, html.parent());
        Element head = html.getFirstChild();
        assertEquals("head", head.tagName());
        Element body = html.getLastChild();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertEquals("head", head.tagName());
        assertSame(document, head.parent());
        assertNull(head.previousSibling());
        assertNull(head.nextSibling());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertEquals("body", body.tagName());
        assertSame(document, body.parent());
        assertNull(body.previousSibling());
        assertNull(body.nextSibling());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text().trim());
    }

    @Test
    public void testTitleMethodWithNullArgument() {
        Document document = new Document("baseUri");
        try {
            document.title(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(document.title());
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertSame(document.baseUri(), element.baseUri());
        assertNull(element.parent());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Text 1");
        TextNode textNode2 = body.appendText("Text 2");
        TextNode textNode3 = head.appendText("Text 3");

        document.normalise();

        List<Node> bodyChildren = new ArrayList<>(body.childNodes());
        assertEquals(3, bodyChildren.size());
        assertEquals(textNode1.text().trim(), body.getFirstChild().text().trim());
        assertEquals(textNode2.text().trim(), body.getLastChild().text().trim());
        assertNull(body.getFirstChild().nextSibling());
        assertNull(body.getLastChild().previousSibling());

        Element titleElement = document.getElementsByTag("title").first();
        assertNull(titleElement);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName("html", null);
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element headChild = head.appendElement("div");
        Element bodyChild = body.appendElement("div");

        Element foundElement = document.findFirstElementByTagName("div", body);
        assertSame(bodyChild, foundElement);

        foundElement = document.findFirstElementByTagName("div", head);
        assertSame(headChild, foundElement);

        foundElement = document.findFirstElementByTagName("div", null);
        assertNull(foundElement);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        body.appendText("Text 1");
        body.appendText("Text 2");

        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head></head><body>Text 1Text 2</body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Text 1Text 2");
        assertEquals("Text 1Text 2", document.text().trim());
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsClass() {
        OutputSettings outputSettings = new OutputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", outputSettings.charset().name());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(5);
        assertEquals(5, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsGetMethods() {
        OutputSettings outputSettings = new OutputSettings();
        Charset charset = Charset.forName("UTF-8");
        outputSettings.charset(charset);
        assertEquals(charset, outputSettings.charset());
        assertEquals(charset.newEncoder(), outputSettings.encoder());
    }

}