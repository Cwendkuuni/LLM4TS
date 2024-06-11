import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        // Create a new empty Document
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        // Create a shell document
        Document shellDocument = Document.createShell("http://example.com");

        // Verify html, head, and body elements are present
        assertNotNull(shellDocument.getElementsByTag("html").first());
        assertNotNull(shellDocument.getElementsByTag("head").first());
        assertNotNull(shellDocument.getElementsByTag("body").first());
    }

    @Test
    public void testHead() {
        // Add head element to document
        document.appendElement("head");

        // Get the head element
        Element headElement = document.head();

        // Verify head element is not null
        assertNotNull(headElement);
    }

    @Test
    public void testBody() {
        // Add body element to document
        document.appendElement("body");

        // Get the body element
        Element bodyElement = document.body();

        // Verify body element is not null
        assertNotNull(bodyElement);
    }

    @Test
    public void testTitle() {
        // Add title element to document
        Element titleElement = document.appendElement("title");
        titleElement.text("Test Title");

        // Get the title of the document
        String title = document.title();

        // Verify title is correct
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        // Set the title of the document
        document.title("New Title");

        // Verify title is set correctly
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        // Create a new element
        Element newElement = document.createElement("div");

        // Verify new element is not null and has correct tag name
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
    }

    @Test
    public void testNormalise() {
        // Normalize the document
        document.normalise();

        // Verify html, head, and body elements are present
        assertNotNull(document.getElementsByTag("html").first());
        assertNotNull(document.getElementsByTag("head").first());
        assertNotNull(document.getElementsByTag("body").first());
    }

    @Test
    public void testOuterHtml() {
        // Add elements to document
        document.appendElement("div");
        document.appendElement("span");

        // Get the outer HTML of the document
        String outerHtml = document.outerHtml();

        // Verify outer HTML is correct
        assertEquals("<div></div><span></span>", outerHtml);
    }

    @Test
    public void testTextNode() {
        // Add text node to document
        document.text("Test Text");

        // Get the body element
        Element bodyElement = document.body();

        // Get the text nodes in the body element
        Elements textNodes = bodyElement.textNodes();

        // Verify text node is present and has correct text
        assertNotNull(textNodes.first());
        assertEquals("Test Text", textNodes.first().text());
    }

    @Test
    public void testNodeName() {
        // Get the node name of the document
        String nodeName = document.nodeName();

        // Verify node name is correct
        assertEquals("#document", nodeName);
    }
}