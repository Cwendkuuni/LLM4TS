
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://www.example.com";
        Document document = Document.createShell(baseUri);

        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        Element head = document.head();

        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        Element body = document.body();

        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);
        document.head().appendElement("title").text("Document Title");

        String title = document.title();

        assertEquals("Document Title", title);
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        document.title("New Title");

        String title = document.title();
        assertEquals("New Title", title);
        assertNotNull(document.head().getElementsByTag("title").first());
        assertEquals("New Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);
        document.appendElement("p").text("Text 1");
        document.appendElement("p").text("Text 2");

        Document normalizedDocument = document.normalise();

        assertSame(document, normalizedDocument);
        assertEquals("html", document.childNodes.get(0).nodeName());
        assertEquals("head", document.childNodes.get(0).childNodes.get(0).nodeName());
        assertEquals("body", document.childNodes.get(0).childNodes.get(1).nodeName());
        assertEquals("Text 1", document.childNodes.get(0).childNodes.get(1).childNodes.get(0).outerHtml().trim());
        assertEquals("Text 2", document.childNodes.get(0).childNodes.get(1).childNodes.get(1).outerHtml().trim());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);
        document.appendElement("p").text("Text");

        String outerHtml = document.outerHtml();

        assertEquals("<p>Text</p>", outerHtml);
    }

    @Test
    public void testText() {
        String baseUri = "http://www.example.com";
        Document document = new Document(baseUri);

        document.text("Text");

        assertEquals("Text", document.body().text());
    }
}
