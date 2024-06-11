import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
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
    public void testTitleWhenTitleElementExists() {
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithNull() {
        try {
            document.title(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());

        Element head = document.head();
        assertEquals("<head></head>", head.outerHtml());

        Element body = document.body();
        assertEquals("<body></body>", body.outerHtml());
    }

    @Test
    public void testNormaliseWhenHtmlElementExists() {
        document.appendElement("html");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWhenHeadElementExists() {
        document.select("html").first().appendElement("head");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWhenBodyElementExists() {
        document.select("html").first().appendElement("body");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWhenTextNodeExists() {
        document.appendText("Test");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());

        assertEquals("Test", document.body().text());
    }

    @Test
    public void testNormaliseWhenTextNodeExistsInHead() {
        document.head().appendText("Test");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());

        assertEquals("Test", document.body().text());
    }

    @Test
    public void testNormaliseWhenTextNodeExistsInRoot() {
        document.appendText("Test");
        document.normalise();
        assertNotNull(document.select("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());

        assertEquals("Test", document.body().text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test");
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}