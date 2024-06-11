import org.junit.Test;
import org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc.createElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc.createElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element elem = doc.createElement("a");
        assertNotNull(elem);
        assertEquals("a", elem.nodeName());
        assertEquals("http://example.com", elem.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendChild(new TextNode("This is some text", ""));
        doc.normalise();
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("This is some text", body.text());

        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        body.appendChild(new Element("div"));
        String html = doc.outerHtml();
        assertEquals("<div></div>", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("This is some text");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("This is some text", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Document.Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Document.Entities.EscapeMode.extended);
        assertEquals(Document.Entities.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset(Charset.forName("UTF-16"));
        assertEquals(Charset.forName("UTF-16"), outputSettings.charset());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}