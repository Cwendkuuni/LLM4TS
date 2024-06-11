import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testDocument() {
        assertNotNull(document);
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");

        assertNotNull(shell);
        assertEquals(Tag.valueOf("html"), shell.tagName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitleEmpty() {
        assertEquals("", document.title());
    }

    @Test
    public void testTitle() {
        document.appendElement("head").appendElement("title").text("Test Title");

        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");

        assertEquals("New Title", document.title());
    }

    @Test
    public void testSetTitleExisting() {
        document.appendElement("head").appendElement("title").text("Old Title");

        document.title("New Title");

        assertEquals("New Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc.select("html"));
        assertNotNull(normalisedDoc.select("head"));
        assertNotNull(normalisedDoc.select("body"));
    }

    @Test
    public void testNormaliseHeadExists() {
        document.appendElement("head");

        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc.select("html"));
        assertNotNull(normalisedDoc.select("head"));
        assertNotNull(normalisedDoc.select("body"));
    }

    @Test
    public void testNormaliseBodyExists() {
        document.appendElement("body");

        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc.select("html"));
        assertNotNull(normalisedDoc.select("head"));
        assertNotNull(normalisedDoc.select("body"));
    }

    @Test
    public void testNormaliseTextNode() {
        document.appendElement("p").text("Test");
        document.normalise();

        assertEquals("", document.body().text());
        assertEquals("Test", document.select("p").first().text());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Sample Text");

        assertEquals("Sample Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}