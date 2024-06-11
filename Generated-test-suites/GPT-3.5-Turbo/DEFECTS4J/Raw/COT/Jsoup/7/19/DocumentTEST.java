import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals(doc.nodeName(), "#root");
        assertEquals(doc.baseUri(), "http://example.com");
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertNotNull(title);
        assertEquals(title, "");

        doc.title("Test Title");
        title = doc.title();
        assertNotNull(title);
        assertEquals(title, "Test Title");
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Document normalizedDoc = doc.normalise();
        assertEquals(normalizedDoc, doc);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        String outerHtml = doc.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        doc.text("Test text");
        String text = body.text();
        assertNotNull(text);
        assertEquals(text, "Test text");
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(outputSettings.escapeMode(), Entities.EscapeMode.base);
        assertEquals(outputSettings.charset(), Charset.forName("UTF-8"));
        assertTrue(outputSettings.prettyPrint());
        assertEquals(outputSettings.indentAmount(), 1);

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(outputSettings.escapeMode(), Entities.EscapeMode.extended);
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(outputSettings.charset(), Charset.forName("ISO-8859-1"));
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        outputSettings.indentAmount(2);
        assertEquals(outputSettings.indentAmount(), 2);
    }
}