package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
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
    public void testDocumentHead() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testDocumentBody() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testDocumentTitle() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testDocumentTitleSetNull() {
        Document doc = new Document("baseUri");
        doc.title(null);
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testDocumentTitleSetEmptyString() {
        Document doc = new Document("baseUri");
        doc.title("");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.nodeName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        body.appendText("Test Test");

        doc.normalise();

        Node node = doc.getElementsByTag("body").first();
        assertEquals(body, node);
        List<Node> textNodes = new ArrayList<>();
        for (Node child : node.childNodes()) {
            if (child instanceof TextNode) {
                textNodes.add(child);
            }
        }
        assertEquals(1, textNodes.size());
        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("Test Test", textNode.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", this);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", body);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", html);
        assertNull(title);

        title = html.findFirstElementByTagName("title", this);
        assertNull(title);

        title = head.findFirstElementByTagName("title", this);
        assertNull(title);

        title = body.findFirstElementByTagName("title", this);
        assertNull(title);

        title = html.findFirstElementByTagName("title", head);
        assertNull(title);

        title = html.findFirstElementByTagName("title", body);
        assertNull(title);

        title = head.findFirstElementByTagName("title", html);
        assertNull(title);

        title = body.findFirstElementByTagName("title", html);
        assertNull(title);

        title = html.findFirstElementByTagName("title", body);
        assertNull(title);

        title = body.findFirstElementByTagName("title", html);
        assertNull(title);

        title = html.findFirstElementByTagName("title", html);
        assertNotNull(title);
        assertEquals("title", title.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertNotNull(outputSettings.encoder());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("UTF-16");
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals("UTF-16", newOutputSettings.charset().name());
        assertNotNull(newOutputSettings.encoder());
        assertTrue(newOutputSettings.prettyPrint());
        assertEquals(1, newOutputSettings.indentAmount());
    }
}