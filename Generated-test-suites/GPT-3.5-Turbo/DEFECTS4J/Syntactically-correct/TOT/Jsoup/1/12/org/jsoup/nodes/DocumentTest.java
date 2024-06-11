import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    
    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().tagName());
        assertEquals("head", shell.head().tagName());
        assertEquals("body", shell.body().parent().tagName());
        assertEquals("body", shell.body().tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitleWithNoTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithTitleElement() {
        document.head().appendElement("title").text("Example Title");
        String title = document.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitleWithNoTitleElement() {
        String title = "Example Title";
        document.title(title);
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text());
    }

    @Test
    public void testSetTitleWithTitleElement() {
        document.head().appendElement("title").text("Old Title");
        String newTitle = "New Title";
        document.title(newTitle);
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(newTitle, titleElement.text());
    }

    @Test
    public void testNormaliseWithExistingElements() {
        document.head().appendElement("meta");
        document.body().appendElement("div");
        Document normalisedDoc = document.normalise();
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals("meta", normalisedDoc.head().child(0).tagName());
        assertEquals("div", normalisedDoc.body().child(0).tagName());
    }

    @Test
    public void testNormaliseWithoutExistingElements() {
        Document normalisedDoc = document.normalise();
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals("head", normalisedDoc.head().parent().tagName());
        assertEquals("body", normalisedDoc.body().parent().tagName());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);
        assertEquals(tagName, element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        String text = "Example text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}