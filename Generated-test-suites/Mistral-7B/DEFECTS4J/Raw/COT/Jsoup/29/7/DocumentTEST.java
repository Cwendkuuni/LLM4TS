package org.jsoup.nodes; // make sure the import statements are correct in your test file

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test for Document class
 */
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
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNull(head);
        document.title("Test Title");
        head = document.head();
        assertNotNull(head);
        assertEquals("title", head.nodeName());
        assertEquals("Test Title", head.text().trim());
    }

    @Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNull(body);
        document.title("Test Title");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        TextNode textNode = body.textNode();
        assertEquals("", textNode.text());
    }

    @Test
    public void testTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.text("Text 1Text 2Text 3");
        document.normalise();
        Element body = document.body();
        TextNode textNode1 = (TextNode) body.childNodes().get(0);
        TextNode textNode2 = (TextNode) body.childNodes().get(1);
        assertEquals("Text 1", textNode1.text());
        assertEquals("Text 2", textNode2.text());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document.body(), null, "tagName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameWithInvalidTagName() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, Tag.valueOf("#root"), "invalidTagName");
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        document.body().appendChild(element);
        Element foundElement = findFirstElementByTagName(document, Tag.valueOf("tagName"), "tagName");
        assertEquals(element, foundElement);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        document.body().text("Test Body");
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        document.body().text("Test Body");
        assertEquals("Test Body", document.text());
    }

    /**
     * Test for clone method
     */
    @Test
    public void testClone() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        document.outputSettings(outputSettings);
        Document clonedDocument = document.clone();
        assertEquals(document, clonedDocument);
        assertNotSame(outputSettings, clonedDocument.outputSettings());
        assertNotSame(outputSettings, document.outputSettings());
    }

    private Element findFirstElementByTagName(Node node, Tag tag, String tagName) {
        if (node.nodeName().equals(tagName)) {
            return (Element) node;
        } else {
            for (Node child : node.childNodes()) {
                Element foundElement = findFirstElementByTagName(child, tag, tagName);
                if (foundElement != null) {
                    return foundElement;
                }
            }
        }
        return null;
    }
}