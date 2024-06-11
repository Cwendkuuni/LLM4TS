import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

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
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);

        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        assertEquals("Existing Title", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleWithEmptyTitleElement() {
        document.head().appendElement("title");
        assertEquals("", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleWithNoTitleElement() {
        assertNull(document.head().getElementsByTag("title").first());
        assertEquals("", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
        assertNotNull(document.head().getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        Tag tag = Tag.valueOf("div");
        assertNotNull(document.createElement(tag.getName()));
    }

    @Test
    public void testNormalise() {
        Document normalisedDocument = document.normalise();

        assertNotNull(normalisedDocument.select("html"));
        assertNotNull(normalisedDocument.head());
        assertNotNull(normalisedDocument.body());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Test Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}