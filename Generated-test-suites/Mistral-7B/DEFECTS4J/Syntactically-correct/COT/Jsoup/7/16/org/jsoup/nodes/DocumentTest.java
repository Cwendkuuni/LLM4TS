package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test class for Document class
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals("baseUri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertSame(Tag.HTML, html.tagName());
        Element head = html.child(0);
        assertSame(Tag.HEAD, head.tagName());
        Element body = html.child(1);
        assertSame(Tag.BODY, body.tagName());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("baseUri");
        assertNull(doc.head());
        Element head = doc.appendChild(new Element(Tag.HEAD));
        Element title = doc.createElement("title");
        title.text("Test Title");
        doc.head().appendChild(title);
        assertEquals("Test Title", doc.head().text());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("baseUri");
        assertNull(doc.body());
        Element body = doc.createElement("body");
        doc.appendChild(body);
        assertEquals("", doc.body().text());
        TextNode textNode = doc.createElement("textNode");
        textNode.text("Test Text");
        doc.body().appendChild(textNode);
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testTitleMethod() {
        Document doc = new Document("baseUri");
        assertNull(doc.title());
        Element titleEl = doc.createElement("title");
        titleEl.text("Test Title");
        doc.appendChild(titleEl);
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleMethodSet() {
        Document doc = new Document("baseUri");
        assertNull(doc.title());
        doc.title("Test Title");
        Element titleEl = doc.getElementsByTag("title").first();
        assertSame(titleEl, doc.head().child(0));
        assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("baseUri");
        Element e = doc.createElement("a");
        assertEquals("a", e.tagName());
        assertEquals("baseUri", e.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("baseUri");
        Element html = doc.appendChild(new Element(Tag.HTML));
        Element head = html.appendChild(new Element(Tag.HEAD));
        Element body = html.appendChild(new Element(Tag.BODY));
        TextNode textNode1 = body.appendChild(new TextNode("Test Text 1"));
        TextNode textNode2 = head.appendChild(new TextNode("Test Text 2"));

        doc.normalise();

        List<Node> expectedBodyChildren = new ArrayList<>();
        expectedBodyChildren.add(textNode1);
        assertEquals(expectedBodyChildren.toArray(), body.children().toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document doc = new Document("baseUri");
        findFirstElementByTagName(null, doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTag() {
        Document doc = new Document("baseUri");
        findFirstElementByTagName("invalidTag", doc);
    }

    @Test
    public void testFindFirstElementByTagNameValidTag() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title");

        Element foundElement = findFirstElementByTagName("head", doc);
        assertSame(head, foundElement);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindFirstElementByTagNameEmptyList() {
        Document doc = new Document("baseUri");
        findFirstElementByTagName("tagName", doc);
    }

    private Element findFirstElementByTagName(String tagName, Node node) {
        if (node.nodeName().equals(tagName)) {
            return (Element) node;
        } else {
            for (Node child : node.childNodes) {
                Element foundElement = findFirstElementByTagName(tagName, child);
                if (foundElement != null) {
                    return foundElement;
                }
            }
        }
        return null;
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("baseUri");

        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        TextNode textNode = doc.createElement("textNode");

        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(textNode);

        doc.appendChild(html);

        assertEquals("<html><head><textNode></textNode></head><body></body></html>", doc.outerHtml());
    }

    @Test(expected = IllegalStateException.class)
    public void testTextMethodSetNullText() {
        Document doc = new Document("baseUri");
        doc.body().text(null);
    }

    @Test
    public void testTextMethodSetEmptyText() {
        Document doc = new Document("baseUri");
        doc.body().text("");
        assertEquals("", doc.body().text());
    }

    @Test
    public void testTextMethodSetText() {
        Document doc = new Document("baseUri");
        doc.body().text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }
}