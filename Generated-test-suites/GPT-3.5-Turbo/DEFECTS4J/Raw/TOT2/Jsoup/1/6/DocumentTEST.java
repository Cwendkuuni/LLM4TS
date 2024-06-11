import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertEquals(doc.baseUri(), "https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head); // head element should be null initially
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNull(body); // body element should be null initially
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        String title = doc.title();
        assertEquals(title, ""); // title should be empty initially
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://www.example.com");
        doc.title("Sample Title");
        String title = doc.title();
        assertEquals(title, "Sample Title");
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
        assertEquals(newElement.tagName(), "div");
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("p").text("Hello");
        doc.normalise();
        Element body = doc.body();
        assertEquals(body.childrenSize(), 1); // body should have only one child after normalisation
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Sample Text");
        assertEquals(doc.body().text(), "Sample Text");
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals(doc.nodeName(), "#document");
    }

}