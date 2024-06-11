import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("html", document.child(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testDocumentTitle() {
        Document document = new Document("http://example.com");
        document.title("My Document");
        assertEquals("My Document", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        assertEquals("a", document.createElement("a").nodeName());
        assertEquals("div", document.createElement("div").nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.createElement("p").text("This is a paragraph.");
        document.createElement("p").text("This is another paragraph.");
        assertEquals("html", document.child(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertEquals("p", document.body().child(0).nodeName());
        assertEquals("p", document.body().child(1).nodeName());
        document.normalise();
        assertEquals("p", document.child(0).nodeName());
        assertEquals("This is a paragraph.", document.body().text().trim());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.createElement("p").text("This is a paragraph.");
        assertEquals("<p>This is a paragraph.</p>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("This is a paragraph.");
        assertEquals("This is a paragraph.", document.body().text());
    }

}