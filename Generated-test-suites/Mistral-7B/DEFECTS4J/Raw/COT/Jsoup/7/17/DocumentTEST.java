package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jasoup.nodes.Document;
import org.jasoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstruction() {
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
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testHeadAccess() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @org.junit.Test
    public void testBodyAccess() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testTitleAccess() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test
    public void testTitleSetTextToEmpty() {
        Document document = new Document("baseUri");
        document.title("");
        String title = document.title();
        assertEquals("", title);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body").appendText("Test Body");

        document.normalise();

        assertEquals("body", document.head().child(0).tagName());
        assertEquals("Test Title", document.head().child(1).text());
        assertEquals("Test Body", document.body().text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, null, "html");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTagName() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, document, "invalidTag");
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element found = findFirstElementByTagName(document, document, "html");
        assertEquals(html, found);
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html").appendElement("body").appendText("Test");
        assertEquals("<html><body>Test</body></html>", html.outerHtml());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTextNullText() {
        Document document = new Document("baseUri");
        document.text(null);
    }

    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        document.text("Test");
        assertEquals("Test", document.body().text());
    }

    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOutputSettingsInvalidCharset() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("InvalidCharset");
    }

    @org.junit.Test
    public void testOutputSettingsCharset() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        Charset charset = Charset.forName("UTF-16LE");
        outputSettings.charset(charset);
        assertEquals(charset, outputSettings.charset());
    }

    @org.junit.Test
    public void testOutputSettingsPrettyPrint() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    private Element findFirstElementByTagName(Document document, Node node, String tag) {
        if (node.nodeName().equals(tag)) {
            return (Element) node;
        } else {
            for (Node child : node.childNodes()) {
                Element found = findFirstElementByTagName(document, child, tag);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

}