
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private static final String BASE_URI = "https://example.com";
    private Document document;

    @Before
    public void setUp() {
        document = new Document(BASE_URI);
    }

    @Test
    public void testDocumentConstructor() {
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals(BASE_URI, document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(BASE_URI);
        assertFalse(shell.select("html").isEmpty());
        assertFalse(shell.select("head").isEmpty());
        assertFalse(shell.select("body").isEmpty());
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

        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());

        document.head().getElementsByTag("title").first().remove();
        assertEquals("", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleWithNull() {
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        String tagName = "a";
        Document.Element element = document.createElement(tagName);
        assertEquals(Tag.valueOf(tagName), element.tag());
        assertEquals(BASE_URI, element.baseUri());
    }

    @Test
    public void testNormalize() {
        document.select("html").remove();
        document.select("head").remove();
        document.select("body").remove();

        assertEquals(0, document.select("html").size());
        assertEquals(0, document.select("head").size());
        assertEquals(0, document.select("body").size());

        document.normalise();

        assertEquals(1, document.select("html").size());
        assertEquals(1, document.select("head").size());
        assertEquals(1, document.select("body").size());
    }

    @Test
    public void testOuterHtml() {
        String expectedHtml = "<head></head><body></body>";
        assertEquals(expectedHtml, document.outerHtml());
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
