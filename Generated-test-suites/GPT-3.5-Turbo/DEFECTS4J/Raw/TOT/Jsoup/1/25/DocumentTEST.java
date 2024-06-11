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
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");

        assertNotNull(shell);
        assertSame(Tag.html, shell.tag());
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        document.appendElement("head");

        assertNotNull(document.head());
        assertSame(Tag.head, document.head().tag());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        document.appendElement("body");

        assertNotNull(document.body());
        assertSame(Tag.body, document.body().tag());
    }

    @Test
    public void testTitleWithNoTitleElement() {
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
    public void testTitleWithNonEmptyTitleElement() {
        document.head().appendElement("title").text("Example Title");

        String title = document.title();

        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitleWithNoTitleElement() {
        document.title("Example Title");

        assertNotNull(document.head().getElementsByTag("title").first());
        assertEquals("Example Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitleWithEmptyTitleElement() {
        document.head().appendElement("title");

        document.title("Example Title");

        assertEquals("Example Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Previous Title");

        document.title("Example Title");

        assertEquals("Example Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        document.appendElement(tagName);

        assertNotNull(document.getElementsByTag(tagName));
        assertSame(Tag.valueOf(tagName), document.getElementsByTag(tagName).first().tag());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html");
        
        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc);
        assertEquals(baseUri, normalisedDoc.baseUri());
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals(Tag.html, normalisedDoc.getElementsByTag("html").first().tag());
        assertEquals(Tag.head, normalisedDoc.head().tag());
        assertEquals(Tag.body, normalisedDoc.body().tag());
    }

    @Test
    public void testText() {
        String text = "Example Text";
        document.text(text);

        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}