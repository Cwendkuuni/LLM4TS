package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

/**
 * DocumentTest.java - JUnit test file for Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.selectFirst("html");
        assertNotNull(html);
        Element head = html.getFirstChild();
        assertNotNull(head);
        Element body = head.getNextSibling();
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertNull(head.text());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
        assertNull(body.text());
    }

    @Test
    public void testTitleMethod_emptyTitle() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleMethod_setTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethod_nullTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        body.appendText("Test Text");
        TextNode textNode = doc.textNode("Test Text Node", "");
        body.appendChild(textNode);

        doc.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(3, bodyChildren.size());
        assertEquals("Test Text", bodyChildren.get(0).text());
        assertEquals("Test Text Node", bodyChildren.get(1).outerHtml());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        body.appendText("Test Text");

        assertEquals("<html><head></head><body>Test Text</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        body.appendText("Test Text");

        doc.text("Test Replaced Text");

        assertEquals("Test Replaced Text", body.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethod_nullBaseUri() {
        Validate.notNull("baseUri is required");
    }

}