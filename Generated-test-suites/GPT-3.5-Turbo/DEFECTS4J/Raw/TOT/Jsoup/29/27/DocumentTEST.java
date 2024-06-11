import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell);
        assertEquals("html", shell.child(0).tagName());
        assertEquals("head", shell.child(0).child(0).tagName());
        assertEquals("body", shell.child(0).child(1).tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertNull(head.parent());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertNull(body.parent());
    }

    @Test
    public void testTitleWithNullTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithEmptyTitleElement() {
        document.head().appendElement("title");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithValidTitleElement() {
        document.head().appendElement("title").text("Example Title");
        String title = document.title();
        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitleWithNullTitle() {
        document.title(null);
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitleWithEmptyTitle() {
        document.title("");
        assertNotNull(document.head().child(0));
        assertEquals("title", document.head().child(0).tagName());
        assertEquals("", document.head().child(0).text());
    }

    @Test
    public void testSetTitleWithExistingTitle() {
        document.head().appendElement("title").text("Old Title");
        document.title("New Title");
        assertEquals("title", document.head().child(0).tagName());
        assertEquals("New Title", document.head().child(0).text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
        assertNotNull(normalizedDoc.getElementsByTag("head"));
        assertNotNull(normalizedDoc.getElementsByTag("body"));
    }

    @Test
    public void testText() {
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(clone.baseUri(), document.baseUri());
        assertEquals(clone.outputSettings().charset().name(), document.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}