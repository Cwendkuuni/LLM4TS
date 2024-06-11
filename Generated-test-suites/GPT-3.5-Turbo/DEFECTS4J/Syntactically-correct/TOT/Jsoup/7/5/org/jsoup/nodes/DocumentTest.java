import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.*;

public class DocumentTest {

    // Test case for constructor
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        Assert.assertEquals(Tag.valueOf("#root"), doc.tag());
        Assert.assertEquals("https://www.example.com", doc.baseUri());
    }

    // Test case for createShell method
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        Assert.assertEquals(Tag.valueOf("#root"), doc.tag());
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("html", doc.head().tagName());
        Assert.assertEquals("head", doc.head().nodeName());
        Assert.assertEquals("body", doc.body().tagName());
        Assert.assertEquals("body", doc.body().nodeName());
    }

    // Test case for head method
    @Test
    public void testHead() {
        Document doc = Document.createShell("https://www.example.com");
        Assert.assertNotNull(doc.head());
        Assert.assertEquals("head", doc.head().tagName());
        Assert.assertEquals("head", doc.head().nodeName());
    }

    // Test case for body method
    @Test
    public void testBody() {
        Document doc = Document.createShell("https://www.example.com");
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("body", doc.body().tagName());
        Assert.assertEquals("body", doc.body().nodeName());
    }

    // Test case for title method - no title element
    @Test
    public void testTitleNoElement() {
        Document doc = Document.createShell("https://www.example.com");
        Assert.assertEquals("", doc.title());
    }

    // Test case for title method - with title element
    @Test
    public void testTitleWithElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title").text("Example Title");
        Assert.assertEquals("Example Title", doc.title());
    }

    // Test case for title method - with empty title element
    @Test
    public void testTitleEmptyElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title");
        Assert.assertEquals("", doc.title());
    }

    // Test case for title method - with whitespace title element
    @Test
    public void testTitleWhitespaceElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title").text("  ");
        Assert.assertEquals("", doc.title());
    }

    // Test case for title method - with multiple title elements
    @Test
    public void testTitleMultipleElements() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title").text("Example Title");
        doc.head().appendElement("title").text("Additional Title");
        Assert.assertEquals("Example Title", doc.title());
    }

    // Test case for title method - with nested title element
    @Test
    public void testTitleNestedElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("div").appendElement("title").text("Example Title");
        Assert.assertEquals("", doc.title());
    }

    // Test case for title method - null title input
    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleNullInput() {
        Document doc = Document.createShell("https://www.example.com");
        doc.title(null);
    }

    // Test case for title method - update existing title element
    @Test
    public void testSetTitleUpdateElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title").text("Example Title");
        doc.title("Updated Title");
        Assert.assertEquals("Updated Title", doc.title());
    }

    // Test case for title method - add new title element
    @Test
    public void testSetTitleAddElement() {
        Document doc = Document.createShell("https://www.example.com");
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
    }

}