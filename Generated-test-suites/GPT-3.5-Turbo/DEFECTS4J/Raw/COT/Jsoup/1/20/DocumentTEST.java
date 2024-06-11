
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        // Create a new Document
        Document document = new Document("https://www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        // Create a valid, empty shell of a document
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document);
        assertEquals(3, document.children().size());
    }

    @Test
    public void testHead() {
        // Accessor to the document's head element
        Document document = Document.createShell("https://www.example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        // Accessor to the document's body element
        Document document = Document.createShell("https://www.example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        // Get the string contents of the document's title element
        Document document = Document.createShell("https://www.example.com");
        String title = document.title();
        assertEquals("", title);

        Element titleElement = document.createElement("title");
        titleElement.text("Example Title");
        document.head().appendChild(titleElement);

        title = document.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitle() {
        // Set the document's title element
        Document document = Document.createShell("https://www.example.com");
        document.title("Example Title");
        assertEquals("Example Title", document.title());
    }

    @Test
    public void testNormalise() {
        // Normalise the document
        Document document = Document.createShell("https://www.example.com");
        Element titleElement = document.createElement("title");
        titleElement.text("Example Title");
        document.head().appendChild(titleElement);

        document.normalise();

        assertEquals(document.head(), document.select("head").first());
        assertEquals(document.body(), document.select("body").first());
        assertEquals(1, document.select("html").size());
        assertEquals(3, document.children().size());
        assertEquals("Example Title", document.select("title").text().trim());
    }

    @Test
    public void testcreateElement() {
        // Create a new Element
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testText() {
        // Set the text of the document's body
        Document document = Document.createShell("https://www.example.com");
        document.body().text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

}
