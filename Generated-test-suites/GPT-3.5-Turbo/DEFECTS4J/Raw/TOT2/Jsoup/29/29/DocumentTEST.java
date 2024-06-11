import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings.Entities.EscapeMode;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("https://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://example.com");
        Element el = doc.createElement("div");
        assertEquals("div", el.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://example.com");
        Element div = doc.appendElement("div");
        div.text("TEST");
        doc.normalise();
        assertTrue(doc.html().contains("TEST"));
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(EscapeMode.extended);
        outputSettings.charset(Charset.forName("UTF-16"));
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(4);

        doc.outputSettings(outputSettings);

        assertEquals(EscapeMode.extended, doc.outputSettings().escapeMode());
        assertEquals(Charset.forName("UTF-16"), doc.outputSettings().charset());
        assertFalse(doc.outputSettings().prettyPrint());
        assertEquals(4, doc.outputSettings().indentAmount());
    }
}