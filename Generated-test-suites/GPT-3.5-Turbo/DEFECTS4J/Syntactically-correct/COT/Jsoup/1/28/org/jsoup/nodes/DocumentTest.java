
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document);

        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = document.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = document.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document document = new Document("https://www.example.com");
        Element head = document.head();
        assertNull(head);

        Element headElement = document.createElement("head");
        document.appendChild(headElement);
        head = document.head();
        assertNotNull(head);
        assertSame(headElement, head);
    }

    @Test
    public void testBody() {
        Document document = new Document("https://www.example.com");
        Element body = document.body();
        assertNull(body);

        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);
        body = document.body();
        assertNotNull(body);
        assertSame(bodyElement, body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://www.example.com");
        String title = document.title();
        assertEquals("", title);

        Element titleElement = document.createElement("title").text("Test Title");
        document.head().appendChild(titleElement);
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("https://www.example.com");

        document.title("New Title");
        String newTitle = document.title();
        assertEquals("New Title", newTitle);

        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());

        document.title("Another Title");
        String anotherTitle = document.title();
        assertEquals("Another Title", anotherTitle);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://www.example.com");

        Element head = document.head();
        Element body = document.body();

        Element div = document.createElement("div");
        div.text("Test Div");
        head.appendChild(div);

        Element span = document.createElement("span");
        span.text("Test Span");
        body.appendChild(span);

        document.normalise();

        head = document.head();
        assertNotNull(head);

        body = document.body();
        assertNotNull(body);

        Element normalizedDiv = head.getElementsByTag("div").first();
        assertNotNull(normalizedDiv);
        assertEquals("", normalizedDiv.text());

        Element normalizedSpan = body.getElementsByTag("span").first();
        assertNotNull(normalizedSpan);
        assertEquals("Test Span", normalizedSpan.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://www.example.com");
        document.createElement("div").text("Test Div");
        String outerHtml = document.outerHtml();
        assertEquals("<div>Test Div</div>", outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("https://www.example.com");
        document.text("Test Body");
        String text = document.body().text();
        assertEquals("Test Body", text);
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://www.example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

}

