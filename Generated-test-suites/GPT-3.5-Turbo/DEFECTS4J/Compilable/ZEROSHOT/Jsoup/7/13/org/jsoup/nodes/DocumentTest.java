package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://example.com");
        assertNull(doc.head());
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://example.com");
        assertNull(doc.body());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        assertEquals("", doc.title());
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("https://example.com");
        assertThrows(NullPointerException.class, () -> {
            doc.title(null);
        });
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.appendElement("head");
        doc.appendElement("body");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(2, doc.childNodes.size());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        Element div = doc.createElement("div");
        div.text("Example Text");
        doc.appendChild(div);
        assertEquals("<div>Example Text</div>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Example Text");
        assertNotNull(doc.body());
        assertEquals("Example Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        assertNotNull(settings);
    }
}