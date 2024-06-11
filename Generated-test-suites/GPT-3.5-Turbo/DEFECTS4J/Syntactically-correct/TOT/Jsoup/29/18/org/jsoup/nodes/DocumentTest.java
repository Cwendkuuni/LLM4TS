import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertTrue(shell.toString().startsWith("<html>"));
        assertTrue(shell.toString().contains("<head>"));
        assertTrue(shell.toString().contains("<body>"));
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetAndGetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitleWithNull() {
        try {
            document.title(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be null", e.getMessage());
        }
    }

    @Test
    public void testTitleElNotPresent() {
        assertNull(document.getElementsByTag("title").first());
        document.title("Test Title");
        assertNotNull(document.getElementsByTag("title").first());
    }

    @Test
    public void testNormalise() {
        document.appendElement("html");
        assertNull(document.head());
        assertNull(document.body());
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.outerHtml(), clone.outerHtml());
        assertNotSame(document, clone);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testText() {
        document.text("Test text");
        assertEquals("Test text", document.text());
    }

    @Test
    public void testTextWithNull() {
        try {
            document.text(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("text must not be null", e.getMessage());
        }
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = document.outputSettings();
        assertNotNull(settings);
        assertEquals(Document.OutputSettings.class, settings.getClass());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}