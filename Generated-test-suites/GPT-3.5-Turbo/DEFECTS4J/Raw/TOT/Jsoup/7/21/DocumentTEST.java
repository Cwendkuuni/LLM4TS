import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Document");
        String title = document.title();
        assertNotNull(title);
        assertEquals("Test Document", title);
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
        // Check if head and body are present
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        // Test case where there is text content in the body
        Element body = document.body();
        body.appendText("Some text");
        Document normalizedDoc = document.normalise();
        String bodyText = normalizedDoc.body().text();
        assertNotNull(bodyText);
        assertEquals("Some text", bodyText);

        // Test case where there is no text content in the body
        bodyText = normalizedDoc.text();
        assertNotNull(bodyText);
        assertEquals("", bodyText);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("", outerHtml);
    }

    @Test
    public void testTextNode() {
        document.text("Some text");
        String bodyText = document.body().text();
        assertNotNull(bodyText);
        assertEquals("Some text", bodyText);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertNotNull(nodeName);
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertNotNull(escapeMode);
    }

    @Test
    public void testSetEscapeModel() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertNotNull(escapeMode);
        assertEquals(Entities.EscapeMode.extended, escapeMode);
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Charset charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testSetCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        Charset charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset.name());
    }

    @Test
    public void testSetCharsetByName() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("UTF-16");
        Charset charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals("UTF-16", charset.name());
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        boolean prettyPrint = outputSettings.prettyPrint();
        assertTrue(prettyPrint);
    }

    @Test
    public void testSetPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        boolean prettyPrint = outputSettings.prettyPrint();
        assertFalse(prettyPrint);
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);
    }

    @Test
    public void testSetIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.indentAmount(2);
        int indentAmount = outputSettings.indentAmount();
        assertEquals(2, indentAmount);
    }

}