import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        Element headElement = doc.createElement("head");
        doc.appendChild(headElement);
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        Element bodyElement = doc.createElement("body");
        doc.appendChild(bodyElement);
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);

        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());

        doc.title(null);
        title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());

        Element body = doc.body();
        body.appendChild(element);

        Element div = doc.getElementsByTag("div").first();
        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals(body, div.parent());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");

        // Add elements outside of body
        Element head = doc.head();
        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        assertNull(doc.body());

        // Normalize the document
        doc.normalise();

        html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        assertNotNull(doc.body());
        assertEquals(1, html.childNodeSize());

        head = doc.head();
        assertNotNull(head);
        assertEquals(1, head.childNodeSize());
        assertEquals("head", head.childNode(0).nodeName());
        assertEquals(title, head.childNode(0));

        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.childNodeSize());
        assertEquals("title", body.childNode(0).nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");

        String outerHtml = doc.outerHtml();
        assertEquals("", outerHtml);

        doc.body().text("Test Body");
        outerHtml = doc.outerHtml();
        assertEquals("Test Body", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");

        doc.text("Test Text");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.childNodeSize());
        assertEquals(" ", body.childNode(0).nodeName()); // Should be text node

        Element textNode = body.childNode(0);
        assertEquals("", textNode.attr("class"));
        assertEquals(" Test Text", textNode.toString()); // Leading space due to text node prepend

        doc.text(null);
        assertEquals(0, body.childNodeSize());
    }
}