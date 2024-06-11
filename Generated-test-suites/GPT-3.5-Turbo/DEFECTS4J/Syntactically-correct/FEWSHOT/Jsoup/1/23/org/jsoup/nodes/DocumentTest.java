
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        Element head = document.createElement("head");
        document.appendChild(head);
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        Element body = document.createElement("body");
        document.appendChild(body);
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        Element title = document.createElement("title");
        title.text("Example Title");
        document.head().appendChild(title);
        assertEquals("Example Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Element root = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        root.appendChild(head);
        root.appendChild(body);
        document.appendChild(root);

        Element textElement = document.createElement("h1");
        TextNode textNode = new TextNode("Example Text", "");
        textElement.appendChild(textNode);
        head.appendChild(textElement);

        document.normalise();

        assertNull(document.head().getElementsByTag("h1").first());
        assertNotNull(document.body().getElementsByTag("h1").first());
        assertEquals("Example Text", document.body().getElementsByTag("h1").first().text());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
        Element root = document.createElement("html");
        document.appendChild(root);
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        assertNull(document.body());
        document.text("Example Text");
        assertNotNull(document.body());
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}
