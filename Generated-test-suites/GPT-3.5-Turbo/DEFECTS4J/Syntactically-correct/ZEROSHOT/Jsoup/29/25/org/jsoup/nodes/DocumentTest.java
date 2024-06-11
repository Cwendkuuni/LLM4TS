import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        String baseUri = "http://www.example.com";
        document = new Document(baseUri);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://www.example.com";
        Document doc = Document.createShell(baseUri);

        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().tagName());
        assertEquals("head", doc.head().parent().tagName());
        assertEquals("body", doc.body().parent().tagName());
        assertEquals("baseUri", baseUri, doc.baseUri());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();

        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();

        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        String title = "Test Title";
        document.title(title);

        assertEquals(title, document.title());
        assertNotNull(document.head());
        assertNotNull(document.getElementsByTag("title"));
        assertEquals(title, document.getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);

        assertNotNull(element);
        assertEquals(tagName, element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();

        assertNotNull(normalizedDoc);
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
        assertEquals("html", normalizedDoc.head().parent().tagName());
        assertEquals("head", normalizedDoc.head().tagName());
        assertEquals("body", normalizedDoc.body().tagName());
    }

    @Test
    public void testNormalizeTextNodes() {
        Element element = new Element(Tag.valueOf("div"), document.baseUri());
        element.appendText("This is a test text");
        element.appendText("Another test text");

        document.normaliseTextNodes(element);

        assertEquals(2, document.body().childNodes().size());
        assertTrue(document.body().childNodes().get(0) instanceof TextNode);
        assertTrue(document.body().childNodes().get(1) instanceof TextNode);
    }

    @Test
    public void testNormalizeStructure() {
        Element htmlEl = new Element(Tag.valueOf("html"), document.baseUri());
        htmlEl.prependElement("head");
        htmlEl.appendElement("body");

        document.normaliseStructure("head", htmlEl);
        document.normaliseStructure("body", htmlEl);

        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
        assertEquals(htmlEl, document.head().parent());
        assertEquals(htmlEl, document.body().parent());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element found = document.findFirstElementByTagName("html", document);
        assertEquals("html", found.tagName());

        found = document.findFirstElementByTagName("head", document);
        assertNull(found);

        Element htmlEl = new Element(Tag.valueOf("html"), document.baseUri());
        htmlEl.append(new Element(Tag.valueOf("head"), document.baseUri()));
        htmlEl.append(new Element(Tag.valueOf("body"), document.baseUri()));
        found = document.findFirstElementByTagName("head", htmlEl);
        assertEquals("head", found.tagName());
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();

        assertNotNull(html);
        assertEquals("", html);
    }

    @Test
    public void testText() {
        String text = "This is a test text";
        document.text(text);

        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();

        assertEquals("#document", nodeName);
    }

    @Test
    public void testClone() {
        Document clone = document.clone();

        assertNotNull(clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outputSettings().charset().name(), clone.outputSettings().charset().name());
        assertEquals(document.outputSettings().escapeMode().name(), clone.outputSettings().escapeMode().name());
        assertEquals(document.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals(document.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);

        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset(Charset.forName("ISO-8859-1"));

        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsCharsetString() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("US-ASCII");

        assertEquals(Charset.forName("US-ASCII"), outputSettings.charset());
        assertEquals("US-ASCII", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);

        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.indentAmount(2);

        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testDocumentQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.quirks);

        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}