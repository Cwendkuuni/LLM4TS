package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any test data here if needed
    }

    @org.junit.Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        // test getEmptyHead
        Document emptyDocument = Document.createShell("baseUri");
        Element emptyHead = emptyDocument.head();
        assertNotNull(emptyHead);
        assertTrue(emptyHead.children().isEmpty());
    }

    @org.junit.Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        // test getEmptyBody
        Document emptyDocument = Document.createShell("baseUri");
        Element emptyBody = emptyDocument.body();
        assertNotNull(emptyBody);
        assertTrue(emptyBody.children().isEmpty());
    }

    @org.junit.Test
    public void testTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertTrue(title.isEmpty());
        document.title("Test Title");
        String title2 = document.title();
        assertEquals("Test Title", title2);
    }

    @org.junit.Test
    public void testTitleSetterNull() {
        Document document = new Document("baseUri");
        try {
            document.title(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
        assertEquals("", element.text());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Text 1");
        TextNode textNode2 = body.appendText("Text 2");

        document.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        Node firstNode = bodyChildren.get(0);
        assertEquals(textNode1.nodeName(), firstNode.nodeName());
        assertEquals(textNode1.text(), firstNode.text());
        Node secondNode = bodyChildren.get(1);
        assertEquals(textNode2.nodeName(), secondNode.nodeName());
        assertEquals(textNode2.text(), secondNode.text());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.createElement("title").text("Title").appendTo(head);

        Element found = document.findFirstElementByTagName("title", this);
        assertEquals(title, found);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("baseUri");
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> document.findFirstElementByTagName("title", null));
        assertEquals("node", exception.getMessage());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTagName() {
        Document document = new Document("baseUri");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> document.findFirstElementByTagName("invalidTagName", this));
        assertEquals("Tag name invalid: invalidTagName", exception.getMessage());
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Text 1");
        TextNode textNode2 = body.appendText("Text 2");

        document.normalise();

        String outerHtml = document.outerHtml();
        String expected = "<html><head></head><body>" + textNode1.text() + textNode2.text() + "</body></html>";
        assertEquals(expected, outerHtml);
    }

    @org.junit.Test
    public void testText() {
        Document document = new Document("baseUri");
        Element body = document.body();
        TextNode textNode = body.text("Text").appendText(" Append Text");

        String text = document.text("Text");
        assertEquals("Text Append Text", text);
    }

    @org.junit.Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test(expected = CloneNotSupportedException.class)
    public void testCloneNullOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = null;
        try {
            Document clone = document.clone();
            OutputSettings cloneOutputSettings = clone.outputSettings();
            assertEquals(outputSettings, cloneOutputSettings);
            fail();
        } catch (CloneNotSupportedException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testClone() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();
        Document clone = document.clone();
        OutputSettings cloneOutputSettings = clone.outputSettings();
        assertEquals(outputSettings, cloneOutputSettings);
    }
}