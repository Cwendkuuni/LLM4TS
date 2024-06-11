package org.jsoup.nodes; // or the package where Document class is located

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());

        Document docShell = Document.createShell("http://example.com");
        assertNotNull(docShell);
        assertEquals("#document", docShell.nodeName());
        assertNotNull(docShell.head());
        assertNotNull(docShell.body());
    }

    @Test
    public void testAccessorMethods() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        String title = doc.title();

        assertNotNull(head);
        assertNotNull(body);
        assertEquals("", title);

        Element newHead = doc.createElement("head");
        doc.appendChild(newHead);

        Element newTitle = doc.createElement("title");
        newTitle.text("Test Title");
        newHead.appendChild(newTitle);

        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");

        doc.title("Test Title 1");
        assertEquals("Test Title 1", doc.title());

        doc.title(null);
        assertEmpty(doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNullBaseUri() {
        Document doc = new Document(null);
        doc.title("Test Title");
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("http://example.com");
        doc.appendChild(new TextNode("Test Text", ""));
        doc.appendChild(new TextNode("Another Test Text", ""));

        doc.normalise();

        Element body = doc.body();
        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        assertEquals(2, textNodes.size());
        assertEquals("Test Text", textNodes.get(0).text());
        assertEquals("Another Test Text", textNodes.get(1).text());
    }
}