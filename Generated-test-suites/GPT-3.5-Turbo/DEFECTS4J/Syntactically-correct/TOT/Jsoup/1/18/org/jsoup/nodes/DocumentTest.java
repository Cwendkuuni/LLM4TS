import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.child(0).nodeName());
        assertEquals("head", shell.child(0).child(0).nodeName());
        assertEquals("body", shell.child(0).child(1).nodeName());
    }

    @Test
    public void testHead() {
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        assertEquals("Existing Title", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("New Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitleWithoutExistingTitleElement() {
        assertNull(document.head().getElementsByTag("title").first());

        document.title("Title");
        assertEquals("Title", document.title());
        assertNotNull(document.head().getElementsByTag("title").first());
        assertEquals("Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        assertNotNull(document.createElement("div"));
        assertEquals("div", document.createElement("div").nodeName());
    }

    @Test
    public void testNormalise() {
        document.body().append("<p>Hello</p>");

        document.normalise();
        assertEquals("p", document.body().child(0).nodeName());
        assertEquals("Hello", document.body().child(0).text());

        document.head().append("<title>Test</title>");

        document.normalise();
        assertEquals("title", document.head().child(0).nodeName());
        assertEquals("Test", document.head().child(0).text());

        document.normalise();
        assertEquals("html", document.child(0).nodeName());
        assertEquals("head", document.child(0).child(0).nodeName());
        assertEquals("body", document.child(0).child(1).nodeName());

        // Try normalizing an empty document
        Document emptyDoc = new Document("https://example.com");
        emptyDoc.normalise();
        assertNotNull(emptyDoc.head());
        assertNotNull(emptyDoc.body());
        assertEquals("html", emptyDoc.child(0).nodeName());
        assertEquals("head", emptyDoc.child(0).child(0).nodeName());
        assertEquals("body", emptyDoc.child(0).child(1).nodeName());
    }

    @Test
    public void testOuterHtml() {
        document.body().append("<p>Hello</p>");
        assertEquals("<p>Hello</p>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.body().append("<p>Hello</p>");
        document.text("New Text");
        assertEquals("New Text", document.body().text());
        assertEquals(0, document.body().children().size());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}