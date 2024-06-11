package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
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
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element headElement = document.head();
        assertNotNull(headElement);

        Element newHeadElement = document.createElement("head");
        document.head(newHeadElement);
        assertEquals(newHeadElement, document.head());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);

        Element newBodyElement = document.createElement("body");
        document.body(newBodyElement);
        assertEquals(newBodyElement, document.body());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        String title = "title";
        document.title(title);
        assertEquals(title, document.title());

        document.title(); // getting title when not set should return empty string
        String emptyTitle = document.title();
        assertTrue(emptyTitle.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodWithNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element newElement = document.createElement("testTag");
        assertNotNull(newElement);
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        Element rootElement = document.createElement("html");
        document.appendChild(rootElement);
        Element bodyElement = document.createElement("body");
        rootElement.appendChild(bodyElement);
        Element titleElement = document.createElement("title");
        titleElement.text("title");
        rootElement.appendChild(titleElement);
        TextNode textNode = document.createElementText("text");
        bodyElement.appendChild(textNode);

        document.normalise();

        List<Node> bodyChildren = bodyElement.childNodes();
        assertEquals(1, bodyChildren.size());
        Node bodyChild = bodyChildren.get(0);
        assertTrue(bodyChild instanceof TextNode);
        TextNode bodyTextNode = (TextNode) bodyChild;
        assertEquals("text", bodyTextNode.text());

        List<Node> titleChildren = titleElement.childNodes();
        assertEquals(0, titleChildren.size());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        String expectedOuterHtml = "<html><head></head><body></body></html>";
        assertEquals(expectedOuterHtml, document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.text("testText");
        assertEquals("testText", document.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethodWithNullText() {
        Document document = new Document("baseUri");
        document.text(null);
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    // Edge cases and exception handling tests:

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentConstructorWithNullBaseUri() {
        new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellWithNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHeadMethodWithNullHeadElement() {
        Document document = new Document("baseUri");
        document.head(null);
        assertNull(document.head());
    }

    @Test
    public void testBodyMethodWithNullBodyElement() {
        Document document = new Document("baseUri");
        document.body(null);
        assertNull(document.body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodWithEmptyTitle() {
        Document document = new Document("baseUri");
        document.title("");
        String emptyTitle = document.title();
        assertTrue(emptyTitle.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodWithWhitespaceTitle() {
        Document document = new Document("baseUri");
        document.title(" ");
        String whitespaceTitle = document.title();
        assertTrue(whitespaceTitle.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodWithLongTitle() {
        Document document = new Document("baseUri");
        String longTitle = "L".repeat(1024);
        document.title(longTitle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementMethodWithNullTagName() {
        Document document = new Document("baseUri");
        document.createElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementMethodWithEmptyTagName() {
        Document document = new Document("baseUri");
        document.createElement("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementMethodWithLongTagName() {
        Document document = new Document("baseUri");
        String longTagName = "T".repeat(1024);
        document.createElement(longTagName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseMethodWithNullElement() {
        Document document = new Document("baseUri");
        document.normalise(null);
    }

    @Test(expected = NullPointerException.class)
    public void testTextMethodWithNullTextNode() {
        Document document = new Document("baseUri");
        document.text(null);
    }
}