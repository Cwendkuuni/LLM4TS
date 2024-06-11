import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertEquals(Tag.html, shell.tagName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithExistingElement() {
        Document spyDocument = Mockito.spy(document);
        Element titleElement = Mockito.mock(Element.class);
        Mockito.when(spyDocument.getElementsByTag("title")).thenReturn(new Elements(titleElement));
        Mockito.when(titleElement.text()).thenReturn("Test Title");

        assertEquals("Test Title", spyDocument.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        document.title(null);
    }

    @Test
    public void testTitleElementAddedToHead() {
        document.title("Test Title");
        assertNotNull(document.head());
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testTitleElementUpdated() {
        Element titleElement = document.head().appendElement("title").text("Existing Title");
        document.title("Test Title");
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testTitleElementNotAddedToHeadIfAlreadyExists() {
        Element existingTitleElement = document.head().appendElement("title");
        document.title("Test Title");
        assertEquals(existingTitleElement, document.head().getElementsByTag("title").first());
    }

    @Test
    public void testTitleElementMovedToHtmlIfNotPresent() {
        Element titleElement = document.head().appendElement("title").text("Existing Title");
        document.head().removeChild(titleElement);
        assertNull(document.head().getElementsByTag("title").first());
        document.title("Test Title");
        assertNull(document.head().getElementsByTag("title").first());
        assertNotNull(document.getElementsByTag("title").first());
        assertEquals(titleElement, document.getElementsByTag("title").first());
        assertEquals(document.head().getElementsByTag("title").size(), 0);
    }

    @Test
    public void testTitleElementNotMovedToHtmlIfAlreadyPresent() {
        Element existingTitleElement = document.getElementsByTag("title").first();
        document.title("Test Title");
        assertEquals(existingTitleElement, document.getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tagName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Element body = document.normalise().body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodes().size());
        Element html = body.parent();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testNormaliseWithExistingHtml() {
        Document spyDocument = Mockito.spy(document);
        Element htmlElement = Mockito.mock(Element.class);
        Mockito.when(spyDocument.getElementsByTag("html")).thenReturn(new Elements(htmlElement));
        Mockito.when(htmlElement.prependElement("head")).thenReturn(new Element(Tag.valueOf("head"), "https://example.com", null));
        Mockito.when(htmlElement.appendElement("body")).thenReturn(new Element(Tag.valueOf("body"), "https://example.com", null));

        Element body = spyDocument.normalise().body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testNormaliseWithExistingHead() {
        Document spyDocument = Mockito.spy(document);
        Element htmlElement = spyDocument.appendElement("html");
        Mockito.when(htmlElement.prependElement("head")).thenReturn(new Element(Tag.valueOf("head"), "https://example.com", null));
        Mockito.when(htmlElement.appendElement("body")).thenReturn(new Element(Tag.valueOf("body"), "https://example.com", null));

        Element head = spyDocument.normalise().head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testNormaliseWithExistingBody() {
        Document spyDocument = Mockito.spy(document);
        Element htmlElement = spyDocument.appendElement("html");
        Mockito.when(htmlElement.prependElement("head")).thenReturn(new Element(Tag.valueOf("head"), "https://example.com", null));
        Mockito.when(htmlElement.appendElement("body")).thenReturn(new Element(Tag.valueOf("body"), "https://example.com", null));

        Element body = spyDocument.normalise().body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        Element body = document.text("Test Body").body();
        assertNotNull(body);
        assertEquals(1, body.childNodes().size());
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Test Body", textNode.getWholeText());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testOutputSettingsCharsetString() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
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
        CharsetEncoder encoder = outputSettings.encoder();
        assertNotNull(encoder);
        assertSame(outputSettings.charset(), encoder.charset());
    }

    @Test
    public void testOutputSettingsGetter() {
        assertNotNull(document.outputSettings());
        assertSame(document.outputSettings(), document.outputSettings());
    }
}