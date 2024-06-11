
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    private String baseUri = "http://example.com";

    @Before
    public void setup() {
        document = new Document(baseUri);
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals(3, shell.body().children().size());
        assertEquals("head", shell.head().nodeName());
        assertEquals("body", shell.body().nodeName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitleWithTitleElement() {
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithoutTitleElement() {
        assertEquals("", document.title());
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Initial Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testSetTitleWithoutTitleElement() {
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("title", document.head().child(0).nodeName());
    }

    @Test
    public void testCreateElement() {
        assertEquals("div", document.createElement("div").nodeName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("head", document.child(0).nodeName());
        assertEquals("body", document.child(1).nodeName());
    }

    @Test
    public void testText() {
        document.body().text("Hello World");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        document.body().appendElement("div");
        assertEquals("<div></div>", document.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        assertNotNull(document.outputSettings());
    }
}

