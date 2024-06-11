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
    public void testTitleMethod_EmptyTitle() {
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
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.appendText("Test Title");

        Element body = html.appendElement("body");
        body.appendText("Test Body");

        document.normalise();

        List<Node> titleNodes = title.childNodes();
        assertEquals(1, titleNodes.size());
        Node titleNode = titleNodes.get(0);
        assertTrue(titleNode instanceof TextNode);

        List<Node> bodyNodes = body.childNodes();
        assertEquals(2, bodyNodes.size());
        Node titleTextNode = bodyNodes.get(0);
        assertTrue(titleTextNode instanceof TextNode);
        assertEquals("Test Title", titleTextNode.text());

        Node bodyTextNode = bodyNodes.get(1);
        assertTrue(bodyTextNode instanceof TextNode);
        assertEquals("Test Body", bodyTextNode.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethod_NullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName("html", null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethod_NullTag() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(null, document);
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod_Exists() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");

        Element found = findFirstElementByTagName("title", document);
        assertNotNull(found);
        assertEquals("title", found.tagName());
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod_DoesNotExist() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");

        // should not find body element
        Element found = findFirstElementByTagName("body", document);
        assertNull(found);
    }

    private Element findFirstElementByTagName(String tagName, Node node) {
        if (node.nodeName().equals(tagName)) {
            return (Element) node;
        } else {
            for (Node child : node.childNodes()) {
                Element found = findFirstElementByTagName(tagName, child);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.appendText("Test Title");

        Element body = document.appendElement("body");
        body.appendText("Test Body");

        document.normalise();

        String htmlString = document.outerHtml();
        String expectedHtmlString = "<html><head><title>Test Title</title></head><body>Test Body</body></html>";
        assertEquals(expectedHtmlString, htmlString);
    }

    @org.junit.Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        body.text("Test Body");

        String bodyText = document.text();
        assertEquals("Test Body", bodyText);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTextMethod_NullBody() {
        Document document = new Document("baseUri");
        document.text(null);
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

        Charset charset = outputSettings.charset();
        assertEquals("UTF-8", charset.name());

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        // charsetEncoder.canEncode("Test String") would be a good test here, but the library does not provide a method to check this

        boolean prettyPrint = outputSettings.prettyPrint();
        assertTrue(prettyPrint);

        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);
    }
}