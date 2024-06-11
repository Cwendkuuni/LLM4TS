import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head);

        Element newHead = doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals(newHead, head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNull(body);

        Element newBody = doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals(newBody, body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.appendElement("head");
        doc.appendElement("div").text("Test Text");
        doc.normalise();
        Element body = doc.body();
        assertEquals(1, body.childNodes.size());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.text("Test Text");
        Element body = doc.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
    }

}