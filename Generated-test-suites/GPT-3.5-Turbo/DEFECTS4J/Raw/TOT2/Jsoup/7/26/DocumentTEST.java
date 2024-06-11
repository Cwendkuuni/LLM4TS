import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("");
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("");
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleWithNull() {
        Document doc = new Document("");
        assertThrows(NullPointerException.class, () -> doc.title(null));
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("");
        doc.normalise();
        assertEquals(1, doc.children().size());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("");
        assertNotNull(doc.createElement("div"));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("");
        assertEquals("#document", doc.nodeName());
    }

}