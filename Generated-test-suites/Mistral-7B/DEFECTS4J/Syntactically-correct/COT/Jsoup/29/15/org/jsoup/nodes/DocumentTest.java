package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
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
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.nodeName());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body");
        TextNode textNode = document.appendText("Test Text");

        document.normalise();

        assertEquals(4, document.childNodeSize());
        assertEquals("html", document.child(0).nodeName());
        Element headElement = (Element) document.child(1);
        assertEquals(1, headElement.childNodeSize());
        assertEquals("head", headElement.nodeName());
        Element titleElement = (Element) headElement.child(0);
        assertEquals("title", titleElement.nodeName());
        assertEquals("Test Title", titleElement.text());
        Element bodyElement = (Element) document.child(2);
        assertEquals("body", bodyElement.nodeName());
        assertEquals(1, bodyElement.childNodeSize());
        TextNode textNodeElement = (TextNode) bodyElement.child(0);
        assertEquals("Test Text", textNodeElement.text());
        Element script = document.createElement("script").text("test()");
        bodyElement.appendChild(script);
        assertEquals(5, document.childNodeSize());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethodWithNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, null, "tagName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameMethodWithInvalidTagName() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, document, "invalidTagName");
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body");

        Element headElement = findFirstElementByTagName(document, document, "head");
        assertEquals(head, headElement);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body");

        assertEquals("<html>\n<head>\n<title>Test Title</title>\n</head>\n<body>\n</body>\n</html>", document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.text("Test Text");
        assertEquals("Test Text", document.outerHtml());
    }

    @Test
    public void testCloneMethod() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body");

        Document clonedDocument = document.clone();

        assertEquals(4, clonedDocument.childNodeSize());
        assertEquals("html", clonedDocument.child(0).nodeName());
        Element headElement = (Element) clonedDocument.child(1);
        assertEquals(1, headElement.childNodeSize());
        assertEquals("head", headElement.nodeName());
        Element titleElement = (Element) headElement.child(0);
        assertEquals("title", titleElement.nodeName());
        assertEquals("Test Title", titleElement.text());
        Element bodyElement = (Element) clonedDocument.child(2);
        assertEquals("body", bodyElement.nodeName());
        assertEquals(0, bodyElement.childNodeSize());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        OutputSettings newOutputSettings = outputSettings.escapeMode(OutputSettings.Entities.EscapeMode.extended);
        assertEquals(OutputSettings.Entities.EscapeMode.extended, newOutputSettings.escapeMode());

        newOutputSettings = newOutputSettings.charset("UTF-16");
        assertEquals("UTF-16", newOutputSettings.charset().name());
    }

    private Element findFirstElementByTagName(Document document, Node node, String tagName) {
        if (node.nodeName().equals(tagName))
            return (Element) node;
        else {
            for (Node child : node.childNodes) {
                Element found = findFirstElementByTagName(document, child, tagName);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

}