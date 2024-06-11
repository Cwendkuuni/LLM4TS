package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testNewDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertEquals(3, doc.childNodes().size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("baseUri", doc.baseUri());
        assertEquals(5, doc.childNodes().size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element elem = doc.createElement("p");
        assertNotNull(elem);
        assertEquals("p", elem.nodeName());
        assertEquals(doc.baseUri(), elem.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.head().appendText("Test Head");
        doc.body().appendText("Test Body");
        doc.body().appendElement("p").appendText("Test Paragraph");

        doc.normalise();

        Element body = doc.body();
        List<Node> bodyChildren = new ArrayList<>(body.childNodes());
        assertEquals(3, bodyChildren.size());
        Node titleNode = doc.getElementsByTag("title").first();
        assertNotNull(titleNode);
        assertEquals("Test Head", titleNode.text());
        assertEquals("Test BodyTest Paragraph", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", doc);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", html);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", body);
        assertNull(title);

        title = doc.findFirstElementByTagName("body", doc);
        assertNotNull(title);

        title = doc.findFirstElementByTagName("body", html);
        assertNull(title);

        title = doc.findFirstElementByTagName("body", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("body", body);
        assertNull(title);

        title = doc.findFirstElementByTagName("html", doc);
        assertNotNull(title);

        title = doc.findFirstElementByTagName("html", html);
        assertNull(title);

        title = doc.findFirstElementByTagName("html", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("html", body);
        assertNull(title);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        TextNode titleText = doc.appendTextNode("Test Title", doc);
        head.appendElement("title").appendText(titleText);
        body.appendText("Test Body");

        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.body().appendText("Test Body");
        doc.text("Test Text");
        assertEquals("Test TextTest Body", doc.text());
    }

    @Test
    public void testTextWithNodes() {
        Document doc = new Document("baseUri");
        doc.body().appendText("Test Body");
        doc.body().appendElement("p").appendText("Test Paragraph");

        doc.text("Test Text");;
        assertEquals("Test TextTest BodyTest Paragraph", doc.text());
    }

    @Test
    public void testTextWithEmptyString() {
        Document doc = new Document("baseUri");
        doc.text("");
        assertEquals("", doc.text());
    }

    @Test
    public void testTextWithNull() {
        Document doc = new Document("baseUri");
        doc.text(null);
        assertEquals("", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testGetOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        doc.outputSettings().prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());
    }
}