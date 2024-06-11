import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitleWithEmptyDocument() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithSetTitle() {
        document.title("Page Title");
        String title = document.title();
        assertEquals("Page Title", title);
    }

    @Test
    public void testTitleWithExistingTitle() {
        document.createElement("title").text("Existing Title");
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Element htmlElement = document.appendElement("html");
        htmlElement.appendElement("head");
        htmlElement.appendElement("body");
        document.normalise();
        Element normalizedHtmlElement = document.getElementsByTag("html").first();
        assertNotNull(normalizedHtmlElement);
        Element normalizedHeadElement = normalizedHtmlElement.getElementsByTag("head").first();
        assertNotNull(normalizedHeadElement);
        Element normalizedBodyElement = normalizedHtmlElement.getElementsByTag("body").first();
        assertNotNull(normalizedBodyElement);
        assertEquals("head", normalizedHeadElement.nodeName());
        assertEquals("body", normalizedBodyElement.nodeName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        document.text("Sample Text");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("Sample Text", bodyElement.text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(org.jsoup.nodes.Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);
        assertEquals(org.jsoup.nodes.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertTrue(outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEncoder() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertNotNull(outputSettings.encoder());
    }

    @Test
    public void testOutputSettingsOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        Document.OutputSettings newOutputSettings = outputSettings.outputSettings();
        assertNotNull(newOutputSettings);
        assertNotSame(outputSettings, newOutputSettings);
    }

    @Test
    public void testOutputSettingsCharsetString() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrintBoolean() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmountInteger() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeModeEntityMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);
        assertEquals(org.jsoup.nodes.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharsetCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.charset(java.nio.charset.Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsEncoderCharsetEncoder() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        java.nio.charset.CharsetEncoder charsetEncoder = java.nio.charset.Charset.forName("UTF-8").newEncoder();
        outputSettings.encoder(charsetEncoder);
        assertSame(charsetEncoder, outputSettings.encoder());
    }
}