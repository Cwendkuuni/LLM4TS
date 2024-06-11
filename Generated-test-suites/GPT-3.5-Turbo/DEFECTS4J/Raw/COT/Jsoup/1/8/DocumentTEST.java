
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        String title = doc.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://example.com");
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc.createElement("div"));
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://example.com");
        doc.normalise();
        assertEquals(1, doc.select("html").size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Example Text");
        assertEquals("Example Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        assertEquals("#document", doc.nodeName());
    }
}

