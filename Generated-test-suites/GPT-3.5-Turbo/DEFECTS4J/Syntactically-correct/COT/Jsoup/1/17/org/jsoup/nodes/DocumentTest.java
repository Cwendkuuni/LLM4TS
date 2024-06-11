import org.junit.Test;
import org.jsoup.nodes.Document;

public class DocumentTest {

    // Test for Document(String baseUri)
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        String expectedBaseUri = "http://example.com";
        assertEquals(expectedBaseUri, doc.baseUri());
        assertEquals("#root", doc.nodeName());
    }

    // Test for static Document createShell(String baseUri)
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");

        // Check if document contains html, head, and body elements
        assertNotNull(doc.getElementsByTag("html").first());
        assertNotNull(doc.getElementsByTag("head").first());
        assertNotNull(doc.getElementsByTag("body").first());

        // Check if document's baseUri is set correctly
        String expectedBaseUri = "http://example.com";
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    // Test for Element head()
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element headElement = doc.head();

        // Check if head element is retrieved correctly
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    // Test for Element body()
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element bodyElement = doc.body();

        // Check if body element is retrieved correctly
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    // Test for String title()
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();

        // Check if empty title is returned when no title element is present
        assertEquals("", title);

        // Add title element and check if it's returned correctly
        doc.head().appendElement("title").text("Example Title");

        title = doc.title();
        assertEquals("Example Title", title);
    }

    // Test for void title(String title)
    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");

        // Add title element and set its text
        doc.title("New Title");

        // Check if title is set correctly
        String expectedTitle = "New Title";
        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(expectedTitle, titleElement.text());
    }

    // Test for Element createElement(String tagName)
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");

        // Create a new element with tag name "div"
        Element divElement = doc.createElement("div");

        // Check if the new element is created correctly with the given tag name
        assertNotNull(divElement);
        assertEquals("div", divElement.nodeName());
    }

    // Test for Document normalise()
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        // Test normalisation logic
        doc.appendElement("p").text("This is a paragraph.");
        doc.appendElement("h1").text("This is a heading.");
        doc.normalise();

        // Check if text nodes are moved into body and order is maintained
        Element bodyElement = doc.body();
        assertEquals("This is a paragraph. This is a heading.", bodyElement.text());
    }

    // Test for String outerHtml()
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");

        // Test outerHtml() returns the same value as html() since there is no outer wrapper tag
        String expectedHtml = "<head></head><body></body>";
        assertEquals(expectedHtml, doc.outerHtml());
    }

    // Test for Element text(String text)
    @Test
    public void testSetText() {
        Document doc = new Document("http://example.com");

        // Set text of body element and check if it's set correctly
        doc.text("Example Text");
        Element bodyElement = doc.body();
        assertEquals("Example Text", bodyElement.text());
    }

    // Test for String nodeName()
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");

        // Check if nodeName() returns "#document"
        assertEquals("#document", doc.nodeName());
    }
}