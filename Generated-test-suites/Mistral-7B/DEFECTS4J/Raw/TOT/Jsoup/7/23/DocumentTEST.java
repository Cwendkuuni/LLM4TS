package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    /**
     * Test creating an empty document.
     */
    @Test
    public void testCreateEmptyDocument() {
        Document emptyDoc = new Document("http://example.com");
        assertEquals("#root", emptyDoc.tagName());
        assertNull(emptyDoc.head());
        assertNull(emptyDoc.body());
    }

    /**
     * Test creating a shell document.
     */
    @Test
    public void testCreateShellDocument() {
        Document shellDoc = Document.createShell("http://example.com");
        assertEquals("#html", shellDoc.tagName());
        Element html = shellDoc.head();
        assertEquals("#head", html.tagName());
        assertEquals("#body", shellDoc.body().tagName());
    }

    /**
     * Test accessing head and body elements.
     */
    @Test
    public void testAccessElements() {
        document.title("Test Document");
        Element head = document.head();
        assertEquals("#title", head.tagName());
        assertEquals("Test Document", head.text().trim());

        Element body = document.body();
        assertNull(body.text());
    }

    /**
     * Test setting and getting title.
     */
    @Test
    public void testSettingAndGettingTitle() {
        document.title("Test Document");
        assertEquals("Test Document", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    /**
     * Test creating a new element with baseUri.
     */
    @Test
    public void testCreateElement() {
        Element link = document.createElement("a");
        assertEquals("#a", link.tagName());
        assertEquals(document.baseUri(), link.baseUri());
    }

    /**
     * Test normalization of document.
     */
    @Test
    public void testNormalizeDocument() {
        document.appendText("This is a test ");
        document.appendText("Normalize me!");
        document.appendElement(document.createElement("p")).text("Another test");

        document.normalize();

        List<Node> bodyNodes = document.body().childNodes();
        assertEquals(3, bodyNodes.size());
        TextNode textNode = (TextNode) bodyNodes.get(0);
        assertEquals("This is a test ", textNode.text().trim());
        textNode = (TextNode) bodyNodes.get(1);
        assertEquals("Normalize me!", textNode.text().trim());
        Element pElement = (Element) bodyNodes.get(2);
        assertEquals("#p", pElement.tagName());
        assertEquals("Another test", pElement.text().trim());
    }

    /**
     * Test findFirstElementByTagName.
     */
    @Test
    public void testFindFirstElementByTagName() {
        document.title("Test Document");
        Element title = document.head().getElementsByTag("title").first();
        assertEquals("#title", title.tagName());
        assertEquals("Test Document", title.text().trim());

        title = document.body().getElementsByTag("title").first();
        assertNull(title);

        title = document.getElementsByTag("title").first();
        assertEquals("#title", title.tagName());
        assertEquals("Test Document", title.text().trim());
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtml() {
        document.title("Test Document");
        document.body().text("Test Body Text");

        String html = document.outerHtml();
        String expected = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test Document</title></head><body><p></p></body></html>";
        assertEquals(expected, html);
    }

    /**
     * Test text method.
     */
    @Test
    public void testText() {
        document.title("Test Document");
        document.body().text("Test Body Text");

        String text = document.text();
        String expected = "Test Body Text\n";
        assertEquals(expected, text);
    }

    /**
     * Test setting charset.
     */
    @Test
    public void testSetCharset() {
        Document doc = new Document("http://example.com");
        doc.outputSettings().charset("UTF-16LE");
        assertEquals("UTF-16LE", doc.outputSettings().charset().name());
    }
}