import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
  
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
  
    @Test
    public void testHead() {
        Document document = new Document("example.com");
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Document document = new Document("example.com");
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleWithNoElement() {
        Document document = new Document("example.com");
        String title = document.title();
        assertEquals("", title);
    }
  
    @Test
    public void testTitleNull() {
        Document document = new Document("example.com");
        assertThrows(NullPointerException.class, () -> document.title(null));
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("example.com");
        document.normalise();
        assertNotNull(document.getElementsByTag("html"));
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = Document.createShell("example.com");
        document.normaliseTextNodes(document.head());
        assertNotNull(document.head());
    }

    @Test
    public void testNormaliseTextNodesWithNoText() {
        Document document = Document.createShell("example.com");
        document.normaliseTextNodes(null);
        assertNotNull(document.head());
    }
  
    @Test
    public void testNormaliseTextNodesWithTextNode() {
        Document document = Document.createShell("example.com");
        Element element = document.createElement("p");
        element.appendChild(new TextNode("Test Text"));
        document.normaliseTextNodes(element);
        assertEquals("Test Text", element.text());
        assertNotNull(document.body());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("example.com");
        String outerHtml = document.outerHtml();
        assertEquals(document.html(), outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("example.com");
        document.text("Test Text");
        String text = document.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testTextWithEmptyString() {
        Document document = Document.createShell("example.com");
        document.text("");
        String text = document.text();
        assertEquals("", text);
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertEquals(Entities.EscapeMode.base, escapeMode);
    }

    @Test
    public void testEscapeModeSet() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertEquals(Entities.EscapeMode.extended, escapeMode);
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testCharsetSet() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testCharsetSetWithName() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("ISO-8859-1");
        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean prettyPrint = outputSettings.prettyPrint();
        assertTrue(prettyPrint);
    }

    @Test
    public void testPrettyPrintSet() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        boolean prettyPrint = outputSettings.prettyPrint();
        assertFalse(prettyPrint);
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);
    }

    @Test
    public void testIndentAmountSet() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.indentAmount(4);
        int indentAmount = outputSettings.indentAmount();
        assertEquals(4, indentAmount);
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
}