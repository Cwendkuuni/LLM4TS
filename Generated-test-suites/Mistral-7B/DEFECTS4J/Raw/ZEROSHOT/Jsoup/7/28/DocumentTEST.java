package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 *
 * @author YourName
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "baseUri";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("baseUri");
        document.title(null);
        assertNull(document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.createElement("head");
        html.appendElement(head);
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendElement(title);
        Element body = document.createElement("body");
        html.appendElement(body);
        TextNode textNode = document.createElementTextNode("Test Text");
        body.appendChild(textNode);

        Document normalisedDocument = document.normalise();
        Element normalisedHead = normalisedDocument.head();
        Element normalisedTitle = normalisedHead.getElementsByTagName("title").first();
        Element normalisedBody = normalisedDocument.body();

        assertNotNull(normalisedHead);
        assertNotNull(normalisedTitle);
        assertNotNull(normalisedBody);
        assertEquals("Test Title", normalisedTitle.text());
        assertEquals("Test Text", normalisedBody.firstTextChild().text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        html.appendElement(head);
        html.appendElement(body);
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendElement(title);
        Element p = document.createElement("p");
        body.appendElement(title);
        body.appendElement(p);

        Element foundTitle = document.findFirstElementByTagName("title", document);
        Element foundP = document.findFirstElementByTagName("p", document);

        assertNotNull(foundTitle);
        assertNotNull(foundP);
        assertEquals("title", foundTitle.tagName());
        assertEquals("p", foundP.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.createElement("head");
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendElement(title);
        Element body = document.createElement("body");
        html.appendElement(head);
        html.appendElement(body);
        TextNode textNode = document.createElementTextNode("Test Text");
        body.appendChild(textNode);

        String htmlString = document.outerHtml();

        String expectedHtmlString = "<html><head><title>Test Title</title></head><body>" +
                "Test Text" +
                "</body></html>";

        assertEquals(expectedHtmlString, htmlString);
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Text");

        String text = document.text();

        assertEquals("Test Text", text);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        outputSettings.charset("ISO-8859-1");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);

        assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), document.outputSettings().charset());
        assertFalse(document.outputSettings().prettyPrint());
        assertEquals(2, document.outputSettings().indentAmount());
    }
}