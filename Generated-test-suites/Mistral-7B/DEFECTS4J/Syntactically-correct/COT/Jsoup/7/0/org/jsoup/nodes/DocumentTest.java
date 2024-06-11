package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        Element html = doc.head();
        assertNotNull(html);
        Element head = html.head();
        assertNotNull(head);
        Element body = html.body();
        assertNotNull(body);
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyAccessor() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleAccessor_EmptyTitle() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleAccessor_SetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("div");
        assertNotNull(element);
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Test Text");
        doc.normalise();
        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        Node titleNode = bodyChildren.get(0);
        assertEquals("Test Title", titleNode.text());
        TextNode textNode = (TextNode) bodyChildren.get(1);
        assertEquals(" Test Text", textNode.text());
    }

    @Test (expected = NullPointerException.class)
    public void testFindElementByTagName_NullNode() {
        Document doc = new Document("baseUri");
        findFirstElementByTagName("head", null);
    }

    @Test
    public void testFindElementByTagName_ValidTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = html.appendElement("title");
        Element headTitle = doc.findFirstElementByTagName("title", html);
        assertEquals(title, headTitle);
    }

    @Test
    public void testFindElementByTagName_InvalidTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        assertNull(doc.findFirstElementByTagName("invalidTag", html));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Test Text");
        String htmlString = doc.outerHtml();
        String expectedHtmlString = "<html><head><title>Test Title</title></head><body>Test Text</body></html>";
        assertEquals(expectedHtmlString, htmlString);
    }

    // Add test cases for OutputSettings methods here

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        body.text("Test Text");
        String text = doc.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

}