package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testEmptyDocument() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertNull(document.title());
        assertNull(document.head());
        assertNull(document.body());
        assertTrue(document.outputSettings().prettyPrint());
        assertTrue(document.outputSettings().escapeMode() == Entities.EscapeMode.base);
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertNotNull(document.title());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertTrue(document.outputSettings().prettyPrint());
        assertTrue(document.outputSettings().escapeMode() == Entities.EscapeMode.base);
    }

    @Test
    public void testGetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertNotNull(document.title());
        assertTrue(document.title().equals("Test Title"));
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title 1");
        document.title("Test Title 2");
        assertNotNull(document.title());
        assertTrue(document.title().equals("Test Title 2"));
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertNotNull(element);
        assertTrue(element.baseUri().equals(document.baseUri()));
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.createElement("title").text("Test Title");
        head.appendChild(title);
        body.text("Test Body ");

        document.normalise();

        Element headInDocument = document.head();
        Element bodyInDocument = document.body();
        TextNode titleText = (TextNode) headInDocument.getElementsByTag("title").first().getElementsByTag("text").first();
        assertNotNull(titleText);
        assertTrue(titleText.text().trim().equals("Test Title"));
        assertTrue(bodyInDocument.text().trim().equals("Test Body "));
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element headElement = document.findFirstElementByTagName("head", document);
        Element bodyElement = document.findFirstElementByTagName("body", document);

        assertNotNull(headElement);
        assertNotNull(bodyElement);
        assertTrue(headElement.equals(head));
        assertTrue(bodyElement.equals(body));
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Body");

        String outerHtml = document.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>";

        assertTrue(outerHtml.equals(expected));
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.appendElement("body");
        body.text("Test Body");

        String text = document.text();
        String expected = "Test Body";

        assertTrue(text.equals(expected));
    }

    @Test
    public void testCloneDocument() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Body");

        Document clonedDocument = document.clone();

        Element headClone = clonedDocument.head();
        Element bodyClone = clonedDocument.body();
        TextNode titleTextClone = (TextNode) headClone.getElementsByTag("title").first().getElementsByTag("text").first();

        assertNotNull(headClone);
        assertNotNull(bodyClone);
        assertNotNull(titleTextClone);
        assertTrue(titleTextClone.text().trim().equals("Test Title"));
        assertTrue(bodyClone.text().trim().equals("Test Body"));
    }

    @Test
    public void testCreateDocumentWithNullBaseUri() {
        try {
            Document document = new Document(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetElementsByTag() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element p = document.createElement("p");
        body.appendElement(p);

        List<Element> elements = document.getElementsByTag("p");
        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(p);

        for (Element element : elements) {
            assertTrue(element.equals(p));
            expectedElements.remove(element);
        }
        assertTrue(expectedElements.isEmpty());
    }

    @Test
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("baseUri");

        assertNull(document.findFirstElementByTagName("head", null));
    }

    @Test
    public void testFindFirstElementByTagNameNullTag() {
        Document document = new Document("baseUri");

        assertNull(document.findFirstElementByTagName(null, document));
    }

    @Test
    public void testNormaliseTextNodesNullElement() {
        Document document = new Document("baseUri");

        assertNull(document.normaliseTextNodes(null));
    }

    @Test
    public void testNormaliseStructureNullTag() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        document.normaliseStructure(null, html);
    }

    @Test
    public void testNormaliseStructureNullHtmlElement() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        document.normaliseStructure("head", null);
    }
}