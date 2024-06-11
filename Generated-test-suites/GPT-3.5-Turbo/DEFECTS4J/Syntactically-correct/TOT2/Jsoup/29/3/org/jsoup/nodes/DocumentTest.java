import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("example.com");
        assertNotNull(doc);
        assertEquals("html", doc.child(0).tagName());
        assertEquals(2, doc.child(0).children().size());
    }

    @Test
    public void testHead() {
        Document doc = new Document("example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("example.com");
        Element body = doc.body();
        assertNull(body);

        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("example.com");
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("example.com");
        assertThrows(IllegalArgumentException.class, () -> {
            doc.title(null);
        });
    }

    // Add more test cases here for other methods in the Document class

}