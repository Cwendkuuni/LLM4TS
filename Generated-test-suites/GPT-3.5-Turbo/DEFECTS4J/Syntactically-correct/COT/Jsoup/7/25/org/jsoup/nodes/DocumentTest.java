
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    // Method: Document(String)
    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        String baseUri = document.baseUri();

        assertEquals("http://example.com", baseUri);
    }

    // Method: createShell(String)
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);

        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = document.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = document.getElementsByTag("body").first();
        assertNotNull(body);
    }

    // Method: head()
    @Test
    public void testHead() {
        Document document = new Document("http://example.com");

        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    // Method: body()
    @Test
    public void testBody() {
        Document document = new Document("http://example.com");

        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    // Method: title()
    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");

        String title = document.title();
        assertEquals("", title);

        document.head().appendElement("title").text("Page Title");
        title = document.title();
        assertEquals("Page Title", title);
    }

    // Method: title(String)
    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Page Title");

        String title = document.title();
        assertEquals("Page Title", title);
    }

    // Method: createElement(String)
    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");

        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    // Method: normalise()
    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");

        Element div = document.createElement("div");
        div.text("Some text");

        Element body = document.body();
        body.appendChild(div);

        document.normalise();

        body = document.body();
        List<Element> children = body.children();
        
        assertEquals(1, children.size());
        assertEquals("Some text div", body.text());
    }

    // Method: findFirstElementByTagName(String, Node)
    @Test
    public void testFindFirstElementByTagName() {
        Document document = Document.createShell("http://example.com");
        
        Element div = document.createElement("div");
        Element span = document.createElement("span");
        Element p = document.createElement("p");

        div.appendChild(span);
        div.appendChild(p);

        Element firstElement = document.findFirstElementByTagName("div", document);
        assertNotNull(firstElement);
        assertEquals("div", firstElement.tagName());

        firstElement = document.findFirstElementByTagName("span", document);
        assertNotNull(firstElement);
        assertEquals("span", firstElement.tagName());

        firstElement = document.findFirstElementByTagName("p", document);
        assertNotNull(firstElement);
        assertEquals("p", firstElement.tagName());
    }

    // Method: outerHtml()
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.body().appendElement("div").text("Some text");

        String outerHtml = document.outerHtml();
        assertEquals("<body><div>Some text</div></body>", outerHtml);
    }

    // Method: text(String)
    @Test
    public void testSetText() {
        Document document = new Document("http://example.com");

        document.body().appendElement("div").text("Some text");

        document.text("New text");

        String text = document.body().text();
        assertEquals("New text", text);
    }
    
    // Method: nodeName()
    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");

        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    // Method: outputSettings()
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document("http://example.com").outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

}
