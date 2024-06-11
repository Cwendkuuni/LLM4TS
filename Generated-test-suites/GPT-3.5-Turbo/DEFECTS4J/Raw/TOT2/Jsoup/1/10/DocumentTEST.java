import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
        assertEquals(Tag.valueOf("html"), doc.tag());
        assertEquals("html", doc.nodeName());
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyAccessor() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Add assertion for normalisation
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        String outerHtml = doc.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Some text");
        assertEquals("Some text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    // Add more test cases for edge cases and exceptions as needed

}