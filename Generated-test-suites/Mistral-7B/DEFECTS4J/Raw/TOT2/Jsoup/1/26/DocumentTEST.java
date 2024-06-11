
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShellmethod() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleMethod_EmptyTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleMethod_SetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethod_SetTitle_NullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test Paragraph");

        doc.normalise();

        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        Element body = doc.body();
        Element p = body.getElementsByTag("p").first();

        assertEquals("Test Title", title.text());
        assertEquals("Test Paragraph", p.text());
    }

    @Test
    public void testNormaliseElementMethod() {
        Document doc = new Document("http://example.com");
        Element root = doc.appendElement("html");
        Element head = root.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = root.appendElement("body");
        Element paragraph = body.appendElement("p").text("Test Paragraph");
        TextNode textNode = root.appendText("Test Text");

        doc.normalise(head);

        assertEquals(3, root.childNodes().size());
        Element titleInBody = body.getElementsByTag("title").first();
        assertEquals("Test Title", titleInBody.text());
        assertEquals("Test Text", textNode.text());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test Paragraph");

        String outerHtml = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", outerHtml);
    }

    @Test
    public void testTextMethod_SetText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("body").appendElement("p");
        doc.text("Test Text");

        assertEquals("Test Text", doc.text().getTextContent());
    }

    @Test
    public void testTextMethod_SetText_EmptyString() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test Text");
        doc.text("");

        assertTrue(doc.text().isEmpty());
    }

    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}
