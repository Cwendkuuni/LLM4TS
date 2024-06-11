import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);

        Element html = document.getElementsByTag("html").first();
        Element head = document.getElementsByTag("head").first();
        Element body = document.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNull(head);

        Element html = document.getElementsByTag("html").first();
        html.appendElement("head");

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNull(body);

        Element html = document.getElementsByTag("html").first();
        html.appendElement("body");

        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");

        String title = document.title();
        assertEquals("", title);

        Element head = document.head();
        head.appendElement("title").text("Test Title");

        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        
        document.title("New Title");
        
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");

        document.appendElement("p").text("Text1");
        document.appendElement("p").text("Text2");

        document.normalise();

        Element body = document.body();
        assertNotNull(body);

        assertEquals(2, body.children().size());

        Element firstChild = body.child(0);
        assertNotNull(firstChild);
        assertEquals("Text1", firstChild.text());

        Element secondChild = body.child(1);
        assertNotNull(secondChild);
        assertEquals("Text2", secondChild.text());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");

        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testTextNode() {
        Document document = new Document("http://example.com");

        document.text("Sample Text");

        Element body = document.body();
        assertNotNull(body);
        assertEquals("Sample Text", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");

        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }

}