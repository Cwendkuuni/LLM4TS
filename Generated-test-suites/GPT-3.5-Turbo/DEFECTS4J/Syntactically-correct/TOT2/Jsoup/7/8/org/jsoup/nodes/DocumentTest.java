import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        assertNotNull(document);
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        document.appendElement("p").text("Test Paragraph");
        document.normalise();
        Elements bodyChildren = document.body().children();
        assertEquals(1, bodyChildren.size());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("https://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}