import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class DocumentTest {

    // Test case for the Document constructor
    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for the createShell method
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    // Test case for the head method
    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    // Test case for the body method
    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    // Test case for the title method
    @Test
    public void testTitle() {
        // Test when title element exists
        Document doc1 = Document.createShell("http://example.com");
        doc1.head().appendElement("title").text("Page Title");
        assertEquals("Page Title", doc1.title());

        // Test when title element does not exist
        Document doc2 = Document.createShell("http://example.com");
        assertEquals("", doc2.title());
    }

    // Test case for the title method with a null parameter
    @Test(expected = NullPointerException.class)
    public void testTitleWithNull() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    // Test case for the createElement method
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    // Test case for the normalise method
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element rootElement = doc.select("html").first();
        Element headElement = doc.select("head").first();
        Element bodyElement = doc.select("body").first();

        // Test when html, head, and body elements are not present
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.select("head").first());
        assertNotNull(doc.select("body").first());

        // Test when html element is present but head and body elements are not present
        rootElement.empty();
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.select("head").first());
        assertNotNull(doc.select("body").first());

        // Test when html and head elements are present but body element is not present
        headElement.remove();
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.select("head").first());
        assertNotNull(doc.select("body").first());
    }

    // Test case for the outerHtml method
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        String expectedOutput = "<html>\n"
                                + "<head></head>\n"
                                + "<body></body>\n"
                                + "</html>";
        assertEquals(expectedOutput, doc.outerHtml());
    }

    // Test case for the text method
    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        body.appendElement("div").text("Hello World");

        doc.text("Test Text");

        assertEquals("Test Text", body.text());
        assertEquals("Test Text", doc.html());
    }

    // Test case for the nodeName method
    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

}