
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        Element headElement = document.appendElement("head");
        assertNotNull(document.head());
        assertEquals(headElement, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        Element bodyElement = document.appendElement("body");
        assertNotNull(document.body());
        assertEquals(bodyElement, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        assertEquals("New Title", document.title());
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        assertNull(document.select("html").first());
        assertNull(document.head());
        assertNull(document.body());
        document.normalise();
        assertNotNull(document.select("html").first());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testOuterHtml() {
        document.text("Test Document");
        assertEquals("Test Document", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test Document");
        assertEquals("Test Document", document.text());
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("Test Document", bodyElement.text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}
