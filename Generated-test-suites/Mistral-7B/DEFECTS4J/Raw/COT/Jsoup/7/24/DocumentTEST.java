package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any state here, if necessary
    }

    // Test for constructor
    @org.junit.Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    // Test for createShell method
    @org.junit.Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.getFirstElementChild();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = head.getNextElementSibling();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    // Test for head and body accessor methods
    @org.junit.Test
    public void testHeadAndBodyAccessorMethods() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    // Test for title method
    @org.junit.Test
    public void testTitleMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element title = doc.title();
        assertNull(title);
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title.text());
    }

    // Test for title method with existing title
    @org.junit.Test
    public void testTitleMethodWithExistingTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Original Title");
        Element title = doc.title();
        assertEquals("Original Title", title.text());
        doc.title("New Title");
        title = doc.title();
        assertEquals("New Title", title.text());
    }

    // Test for createElement method
    @org.junit.Test
    public void testCreateElementMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    // Test for normalise method
    @org.junit.Test
    public void testNormaliseMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.appendElement("html").text("Test");
        doc.appendElement("head").appendElement("title").text("Test Title");
        doc.appendElement("body").appendElement("p").text("Test");
        doc.normalise();
        Element body = doc.body();
        assertEquals("Test Test Title\nTest", body.text());
    }

    // Test for findFirstElementByTagName method
    @org.junit.Test
    public void testFindFirstElementByTagNameMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        html.appendElement("html");
        html.appendElement("head").appendElement("title").text("Test Title");
        html.appendElement("body").appendElement("p").text("Test");
        Element title = doc.findFirstElementByTagName("title", html);
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test Title", title.text());
    }

    // Test for outerHtml method
    @org.junit.Test
    public void testOuterHtmlMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test");
        String htmlString = doc.outerHtml();
        assertTrue(htmlString.contains("<html><body><p>Test</p></body></html>"));
    }

    // Test for text method
    @org.junit.Test
    public void testTextMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test");
        doc.text("Test Text");

        // Test for empty text
        assertEquals("Test Text", doc.text());
        assertEquals("Test Text", doc.body().text());

        // Test for non-empty text
        doc.body().appendElement("p").text("New Test");
        assertEquals("Test TextNew Test", doc.text());
    }

    // Test for escapeMode method with base escape mode
    @org.junit.Test
    public void testEscapeModeBase() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.text("<script>alert('Hello')</script>");
        String html = doc.html();
        assertTrue(html.contains("&lt;script&gt;alert('Hello')&lt;/script&gt;"));
    }

    // Test for escapeMode method with extended escape mode
    @org.junit.Test
    public void testEscapeModeExtended() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
        doc.text("<script>alert('Hello')</script>");
        String html = doc.html();
        assertTrue(html.contains("<script>alert('Hello')</script>"));
    }

    // Test for charset method
    @org.junit.Test
    public void testCharsetMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Charset charset = Charset.forName("UTF-16LE");
        doc.outputSettings().charset(charset);
        doc.text("Test");
        String html = doc.html();
        assertTrue(html.contains("<meta charset=\"UTF-16LE\""));
    }

    // Test for indentAmount method
    @org.junit.Test
    public void testIndentAmountMethod() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.outputSettings().indentAmount(3);
        doc.appendElement("html").appendElement("body").appendElement("p").text("Test");
        String html = doc.html();
        String expected = "<html>\n\n<body>\n\n<p>Test\n</p>\n</body>\n</html>";
        assertTrue(html.equals(expected));
    }
}