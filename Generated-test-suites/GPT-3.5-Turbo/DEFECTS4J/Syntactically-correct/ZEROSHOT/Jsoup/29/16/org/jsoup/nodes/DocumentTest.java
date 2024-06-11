import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Test
    public void testConstructor() {
        document = new Document("https://www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        document = Document.createShell("https://www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testHead() {
        document = Document.createShell("https://www.example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        document = Document.createShell("https://www.example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        document = Document.createShell("https://www.example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testEnsureElementInBody() {
        document = Document.createShell("https://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        document = Document.createShell("https://www.example.com");
        document.normalise();
        assertNotNull(document);
    }

    @Test
    public void testOuterHtml() {
        document = Document.createShell("https://www.example.com");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        document = Document.createShell("https://www.example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        document = Document.createShell("https://www.example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        document = Document.createShell("https://www.example.com");
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
    }

    @Test
    public void testOutputSettings() {
        document = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.base);
        outputSettings.charset("UTF-8");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        document = Document.createShell("https://www.example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }
}