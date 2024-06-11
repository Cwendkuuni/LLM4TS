import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull("Document object should not be null", doc);
        assertEquals("Document baseUri should be set correctly", "http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull("Document object should not be null", doc);
        assertNotNull("Document should have an html element", doc.getElementsByTag("html"));
        assertNotNull("Document should have a head element", doc.getElementsByTag("head"));
        assertNotNull("Document should have a body element", doc.getElementsByTag("body"));
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        doc.appendElement("head");
        assertNotNull("head() should return the head element", doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        assertNotNull("body() should return the body element", doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        assertEquals("title() should return the correct title", "Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("title() should return the updated title", "Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        assertNotNull("createElement() should return a new element", doc.createElement("a"));
        assertEquals("createElement() should create an element with the specified tag name", "a", doc.createElement("a").tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        doc.appendElement("head");
        doc.text("Test Text");

        doc.normalise();

        assertEquals("normalise() should move text nodes into the body", "Test Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        assertEquals("outerHtml() should return the html content of the document", "<body></body>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        doc.text("Test Text");
        assertEquals("text() should set the text content of the body", "Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("nodeName() should return the correct node name", "#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        assertNotNull("outputSettings() should return the document's output settings object", doc.outputSettings());
    }

    // Additional test cases for edge cases and exceptions go here

}

