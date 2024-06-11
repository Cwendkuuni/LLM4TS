import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document);
        assertEquals("html", document.head().parent().tagName());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitleWithTitleElementPresent() {
        Document document = Document.createShell("http://www.example.com");
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithoutTitleElementPresent() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://www.example.com");
        document.title("Test Title");
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://www.example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        document.normalise();
        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        Element headElement = document.getElementsByTag("head").first();
        assertNotNull(headElement);
        Element bodyElement = document.getElementsByTag("body").first();
        assertNotNull(bodyElement);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = Document.createShell("http://www.example.com");
        Element element = document.appendElement("div");
        element.appendText("  ");
        element.appendText("Test");
        element.appendText(" ");
        document.normaliseTextNodes(element);
        assertEquals("Test", element.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://www.example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsOutputSettings() {
        Document document = Document.createShell("http://www.example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

}