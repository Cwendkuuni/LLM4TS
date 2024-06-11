import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        // Verify that the baseUri is set correctly
        assert document.baseUri().equals("http://example.com");
        // Verify that the document has a root tag of "#root"
        assert document.nodeName().equals("#root");
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        // Verify that the baseUri is set correctly
        assert document.baseUri().equals("http://example.com");
        // Verify that the document has a root tag of "html"
        assert document.nodeName().equals("html");
        // Verify that the document contains a head element
        assert document.head() != null;
        // Verify that the document contains a body element
        assert document.body() != null;
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        // Verify that the head element is null initially
        assert head == null;
        // Add a head element to the document
        document.appendElement("head");
        head = document.head();
        // Verify that the head element is not null after adding it to the document
        assert head != null;
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        // Verify that the body element is null initially
        assert body == null;
        // Add a body element to the document
        document.appendElement("body");
        body = document.body();
        // Verify that the body element is not null after adding it to the document
        assert body != null;
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        // Verify that the title is an empty string initially
        assert document.title().equals("");
        // Set the title
        document.title("Test Title");
        // Verify that the title is set correctly
        assert document.title().equals("Test Title");
    }

    @Test
    public void testTitleNoElement() {
        Document document = new Document("http://example.com");
        // Remove the title element from the document
        document.getElementsByTag("title").remove();
        // Set the title
        document.title("Test Title");
        // Verify that the title element is created and the title is set correctly
        assert document.title().equals("Test Title");
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        // Verify that the element has the correct tag name
        assert element.tagName().equals("div");
        // Verify that the element has the correct baseUri
        assert element.baseUri().equals("http://example.com");
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        // Add some text nodes outside of the body element
        document.prependChild(new TextNode("Text 1", ""));
        document.appendElement("div").prependChild(new TextNode("Text 2", ""));
        document.normalise();
        // Verify that the text nodes are moved into the body element
        assert document.body().text().equals("Text 2 Text 1");
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        // Verify that the outerHtml method returns the same result as the html method
        assert document.outerHtml().equals(document.html());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.body().appendText("Hello, world!");
        // Verify that the text method sets the text of the body element correctly
        assert document.text().equals("Hello, world!");
    }
}