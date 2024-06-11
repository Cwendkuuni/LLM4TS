package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;

import static org.jsoup.helper.Validate.*;

public class DocumentTest {

    @org.junit.Test
    public void testCreateDocument() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
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
    public void testTitleMethod_NullTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
    }

    @org.junit.Test
    public void testTitleMethod_SetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.text("Text in root");
        document.body().text("Text in body");
        document.normalise();
        Element body = document.body();
        TextNode textNode = (TextNode) body.getChildNodes().get(0);
        assertEquals("Text in body", textNode.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagName_NullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName("tagName", null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagName_InvalidTagName() {
        Document document = new Document("baseUri");
        findFirstElementByTagName("invalidTagName", document);
    }

    @org.junit.Test
    public void testFindFirstElementByTagName_ValidTagName() {
        Document document = new Document("baseUri");
        Element element = document.createElement("testTag");
        document.appendElement(element);
        Element foundElement = findFirstElementByTagName("testTag", document);
        assertNotNull(foundElement);
        assertEquals("testTag", foundElement.tagName());
    }

    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        document.text("Text in root");
        document.body().text("Text in body");
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.contains("Text in root"));
        assertTrue(outerHtml.contains("Text in body"));
    }

    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.text("Text in body");
        Element body = document.body();
        body.text("Text in body (updated)");
        document.text("Text in body (updated)");
        assertEquals("Text in body (updated)", document.body().text());
    }

    @org.junit.Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testOutputSettings_EscapeMode() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @org.junit.Test
    public void testOutputSettings_Charset() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        Charset charset = Charset.forName("UTF-16");
        outputSettings.charset(charset);
        assertEquals(charset, outputSettings.charset());
        outputSettings.charset("UTF-16BE");
        assertEquals(Charset.forName("UTF-16BE"), outputSettings.charset());
    }

    @org.junit.Test
    public void testOutputSettings_PrettyPrint() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        outputSettings.prettyPrint(true);
        assertTrue(outputSettings.prettyPrint());
    }

    private Element findFirstElementByTagName(String tagName, Node node) {
        if (node.nodeName().equals(tagName))
            return (Element) node;
        else {
            for (Node child: node.childNodes) {
                Element foundElement = findFirstElementByTagName(tagName, child);
                if (foundElement != null)
                    return foundElement;
            }
        }
        return null;
    }
}