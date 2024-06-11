import org.junit.Test;
import org.jsoup.nodes.Document;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.body());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Text in the body ");
        doc.normalise();
        assertEquals("<html><head></head><body>Text in the body </body></html>", doc.outerHtml());
    }
    
    @Test
    public void testTextNode() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Text in the body ");
        doc.normalise();
        assertEquals("<html><head></head><body>Text in the body </body></html>", doc.outerHtml());
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateElementWithNullTagName() {
        Document doc = new Document("http://example.com");
        doc.createElement(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTitleWithNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }
}