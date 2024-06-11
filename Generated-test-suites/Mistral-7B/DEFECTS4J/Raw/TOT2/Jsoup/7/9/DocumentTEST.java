import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        assertEquals("Test Title", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTitleNull() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body>Test<p>Text</p></body></html>");
        doc.normalise();
        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        assertEquals("TestText", body.text());
    }
}