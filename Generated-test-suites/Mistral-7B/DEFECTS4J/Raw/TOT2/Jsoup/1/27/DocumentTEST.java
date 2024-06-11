package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    // Test cases for each public method in Document class

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals(3, document.childElementCount());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.tagName());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleMethodSet() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("http://example.com");
        Element newElement = document.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("http://example.com");
        Element element1 = document.createElement("p");
        Element element2 = document.createElement("p");
        TextNode textNode1 = document.createTextNode("Text 1");
        TextNode textNode2 = document.createTextNode("Text 2");

        document.body().appendChild(element1);
        document.body().appendChild(element2);

        document.normalise();

        List<Node> bodyNodes = document.body().childNodes();
        for (Node node : bodyNodes) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                assertEquals(textNode1.nodeValue(), textNode.nodeValue());
                assertEquals(textNode2.nodeValue(), document.body().lastChild().nodeValue());
            }
        }
    }

    // Edge cases and exception handling tests

    @Test
    public void testDocumentConstructorNullBaseUri() {
        new Document(null);
    }

    @Test
    public void testCreateShellNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHeadMethodNoHeadElement() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNull(headElement);
    }

    @Test
    public void testBodyMethodNoBodyElement() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNull(bodyElement);
    }

    @Test
    public void testTitleMethodNoTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodNullTitle() {
        Document document = new Document("http://example.com");
        document.title(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementMethodNullTagName() {
        Document document = new Document("http://example.com");
        document.createElement(null);
    }
}