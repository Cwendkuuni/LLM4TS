
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DocumentTest {

    private static final String BASE_URI = "https://example.com";

    private final Document document;

    public DocumentTest(Document document) {
        this.document = document;
    }

    @Parameterized.Parameters
    public static Collection<Document> documents() {
        return Arrays.asList(
                new Document(BASE_URI),
                Document.createShell(BASE_URI)
        );
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
    public void testTitle_NoTitleSet_ReturnsEmptyString() {
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_TitleSet_ReturnsTrimmedTitle() {
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testTitle_MultipleTitles_ReturnsLastTitle() {
        document.appendElement("head")
                .appendElement("title")
                .text("Title 1");
        document.appendElement("head")
                .appendElement("title")
                .text("Title 2");

        assertEquals("Title 2", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitle_NullTitle_ThrowsIllegalArgumentException() {
        document.title(null);
    }

    @Test
    public void testNormalise() {
        // Test normalise with no html, head, or body tags
        Document doc = new Document(BASE_URI);
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().parent().tagName());
        assertEquals("html", doc.body().parent().tagName());

        // Test normalise with html and head, but no body tag
        Document doc2 = new Document(BASE_URI);
        doc2.appendElement("html")
                .appendElement("head");
        doc2.normalise();
        assertNotNull(doc2.head());
        assertNotNull(doc2.body());
        assertEquals("html", doc2.head().parent().tagName());
        assertEquals("html", doc2.body().parent().tagName());

        // Test normalise with html, head, and body tags
        Document doc3 = new Document(BASE_URI);
        doc3.appendElement("html")
                .appendElement("head")
                .appendElement("body");
        doc3.normalise();
        assertNotNull(doc3.head());
        assertNotNull(doc3.body());
        assertEquals("html", doc3.head().parent().tagName());
        assertEquals("html", doc3.body().parent().tagName());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document(BASE_URI);
        doc.appendElement("body");

        // Test normaliseTextNodes with text node
        doc.body().text("text");
        doc.normaliseTextNodes(doc.body());
        assertEquals(2, doc.body().childNodes().size());
        assertTrue(doc.body().childNodes().get(1).nodeName().equals("#text"));

        // Test normaliseTextNodes with blank text node
        doc.body().text("");
        doc.normaliseTextNodes(doc.body());
        assertEquals(2, doc.body().childNodes().size());
        assertTrue(doc.body().childNodes().get(1).nodeName().equals("#text"));

        // Test normaliseTextNodes with non-blank text node
        doc.body().text("text 1");
        doc.normaliseTextNodes(doc.body());
        doc.body().text("text 2");
        doc.normaliseTextNodes(doc.body());
        assertEquals(2, doc.body().childNodes().size());
        assertTrue(doc.body().childNodes().get(1).nodeName().equals("#text"));
        assertEquals("text 2", doc.body().childNodes().get(1).outerHtml());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document(BASE_URI);
        doc.appendElement("html")
                .appendElement("head")
                .appendElement("head")
                .appendElement("body")
                .appendElement("body")
                .appendElement("body");

        // Test normaliseStructure with multiple head elements
        doc.normaliseStructure("head", doc.getElementsByTag("html").first());
        assertEquals(1, doc.getElementsByTag("head").size());

        // Test normaliseStructure with multiple body elements
        doc.normaliseStructure("body", doc.getElementsByTag("html").first());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals("html", doc.getElementsByTag("body").first().parent().tagName());
    }

    @Test
    public void testNormaliseStructure_NoDuplicates_ReturnsSameDocument() {
        Document doc = new Document(BASE_URI);
        doc.appendElement("html")
                .appendElement("head")
                .appendElement("body");

        Document result = doc.normaliseStructure("head", doc.getElementsByTag("html").first());

        assertSame(doc, result);
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        assertEquals(tagName, document.createElement(tagName).tagName());
        assertEquals("https://example.com", document.createElement(tagName).baseUri());
    }

    // Other test cases for other methods

}
