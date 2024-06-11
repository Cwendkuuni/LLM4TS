import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testEmptyDocument() {
        Document document = new Document("http://example.com");
        assertEquals("#root", document.tagName()); // Test method tagName()

        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        assertEquals("head", document.head().tagName()); // Test method head()
        assertEquals("body", document.body().tagName()); // Test method body()

        assertEquals("", document.title()); // Test method title()
        document.title("Sample Title");
        assertEquals("Sample Title", document.title());

        Element div = document.createElement("div");
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri()); // Test method baseUri()

        assertEquals(document, document.normalise()); // Test method normalise()

        Element p = document.createElement("p");
        p.appendChild(new TextNode("This is a paragraph", ""));
        body.appendChild(p);

        assertEquals("<html><head><title>Sample Title</title></head><body><p>This is a paragraph</p></body></html>", document.outerHtml()); // Test method outerHtml()

        document.text("New Text");
        assertEquals("New Text", document.body().text()); // Test method text()

        assertEquals("#document", document.nodeName()); // Test method nodeName()
    }

    @Test
    public void testDocumentWithExistingHeadElement() {
        Document document = Document.createShell("http://example.com");

        Element head = document.head();
        assertNotNull(head);

        Element title = head.appendElement("title");
        title.text("Page Title");

        assertNotNull(document.getElementsByTag("head").first());

        assertEquals("Page Title", document.title());

        document.title("New Page Title");
        assertEquals("New Page Title", document.title());
    }

    @Test
    public void testDocumentWithoutHeadElement() {
        Document document = Document.createShell("http://example.com");

        Document normalisedDocument = document.normalise();
        assertEquals(normalisedDocument, document); // Test method normalise()

        Element body = document.body();
        assertNotNull(body);

        document.title("Page Title");
        assertEquals("Page Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element body = html.appendElement("body");

        Element p = document.createElement("p");
        p.appendChild(new TextNode("This is a paragraph", ""));
        html.appendChild(p);

        Document normalisedDocument = document.normalise();
        assertEquals(normalisedDocument, document); // Test method normalise()

        assertNotNull(document.head()); // Test method head()
        assertEquals("<head></head>", document.head().outerHtml());

        assertNotNull(document.body()); // Test method body()
        assertEquals("<body><p>This is a paragraph</p></body>", document.body().outerHtml());

        assertEquals("<html><head></head><body><p>This is a paragraph</p></body></html>", document.outerHtml()); // Test method outerHtml()
    }
}