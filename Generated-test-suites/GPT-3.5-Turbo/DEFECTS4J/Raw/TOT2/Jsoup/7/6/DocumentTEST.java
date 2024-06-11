import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {
    
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleWithNull() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.createElement("div"));
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Add assertions for normalisation results
    }
    
    // Add more test cases for other public methods in Document class

}