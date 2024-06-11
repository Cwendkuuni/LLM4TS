
import static org.junit.Assert.*;
import org.junit.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();
        
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testAccessHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();

        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();

        assertNotNull(head);
    }

    @Test
    public void testAccessBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();

        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();

        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = "Test Title";
        doc.title(title);

        assertEquals(title, doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("div").text("Test Text");
        doc.normalise();

        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element elem = doc.createElement("div");

        assertEquals("div", elem.tagName());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        String text = "Test Text";
        doc.text(text);

        assertEquals(text, doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
    }

}
