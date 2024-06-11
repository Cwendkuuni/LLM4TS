import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals("html", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("https://example.com");
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Document document = new Document("https://example.com");
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("https://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        Document normalizedDocument = document.normalise();
        assertEquals(document, normalizedDocument);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://example.com");
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("https://example.com");
        Element body = document.body();
        assertNotNull(body);
        document.text("Test Text");
        String text = body.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testClone() {
        Document document = new Document("https://example.com");
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.getClass(), clone.getClass());
        assertEquals(document.outerHtml(), clone.outerHtml());
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("https://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(OutputSettings.class, outputSettings.getClass());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("https://example.com");
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
    }
}