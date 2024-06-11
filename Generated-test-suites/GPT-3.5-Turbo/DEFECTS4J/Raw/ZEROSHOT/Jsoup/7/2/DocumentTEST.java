import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        assertEquals("html", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        
        assertNull(head);

        doc.createElement("head");
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        
        assertNull(body);

        doc.createElement("body");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.title());
        
        doc.createElement("title").text("Test");
        assertEquals("Test", doc.title());
        
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element elem = doc.createElement("div");
        
        assertEquals("div", elem.nodeName());
        assertEquals("http://example.com", elem.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        doc.createElement("html");
        assertNull(doc.head());
        assertNull(doc.body());

        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        Element head = doc.head();
        Element body = doc.body();

        doc.normalise();
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());

        doc.createElement("div");
        assertEquals("<div></div>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.text());

        doc.createElement("body");
        doc.text("Hello World");
        assertEquals("Hello World", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        
        assertNotNull(settings);
        
        assertEquals(Document.OutputSettings.EscapeMode.base, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().name());
        assertTrue(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());
        
        settings = settings.escapeMode(Document.OutputSettings.EscapeMode.extended)
                           .charset("ISO-8859-1")
                           .prettyPrint(false)
                           .indentAmount(2);
        
        assertEquals(Document.OutputSettings.EscapeMode.extended, settings.escapeMode());
        assertEquals("ISO-8859-1", settings.charset().name());
        assertFalse(settings.prettyPrint());
        assertEquals(2, settings.indentAmount());
    }
}