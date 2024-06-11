import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("html", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNull(headElement);
        
        document.appendElement("head");
        headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNull(bodyElement);
        
        document.appendElement("body");
        bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
        
        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleEmpty() {
        document.appendElement("head");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            document.title(null);
        });
    }

    @Test
    public void testNormalise() {
        document.appendElement("title");
        document.normalise();
        assertEquals("html", document.select("html").first().nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseTextNode() {
        TextNode textNode = new TextNode("Test Text", "");
        document.appendChild(textNode);
        document.normalise();
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNormaliseEmptyTextNode() {
        TextNode textNode = new TextNode(" ", "");
        document.appendChild(textNode);
        document.normalise();
        assertTrue(document.body().childNodes().isEmpty());
    }

    @Test
    public void testNormaliseNoHead() {
        document.select("head").first().remove();
        document.normalise();
        assertNotNull(document.head());
    }

    @Test
    public void testNormaliseNoBody() {
        document.select("body").remove();
        document.normalise();
        assertNotNull(document.body());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testTextStructure() {
        document.appendElement("p").text("Paragraph");
        document.text("New Text");
        assertEquals("New Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}