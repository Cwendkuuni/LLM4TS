import static org.junit.Assert.*;
import org.junit.Test;

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
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
    }

    // Additional test cases for other methods...

}