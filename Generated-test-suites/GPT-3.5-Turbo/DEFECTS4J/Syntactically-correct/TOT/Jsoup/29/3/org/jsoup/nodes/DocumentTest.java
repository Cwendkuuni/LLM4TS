import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals("#root", document.nodeName());
        assertEquals("https://example.com", document.baseUri());
        assertEquals("html", document.child(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHeadAccessor() {
        Document document = Document.createShell("https://example.com");
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBodyAccessor() {
        Document document = Document.createShell("https://example.com");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitleGetter() {
        Document document = Document.createShell("https://example.com");
        assertEquals("", document.title());

        document.title("Example Title");
        assertEquals("Example Title", document.title());
    }

    @Test
    public void testTitleSetter() {
        Document document = Document.createShell("https://example.com");
        document.title("Example Title");
        assertEquals("Example Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testcreateElement() {
        Document document = Document.createShell("https://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://example.com");
        document.appendChild(new TextNode("Hello"));
        document.normalise();
        assertEquals("Hello", document.body().text());

        Element htmlElement = document.child(0);
        Element headElement = document.head();
        Element bodyElement = document.body();
        assertNotNull(htmlElement);
        assertNotNull(headElement);
        assertNotNull(bodyElement);

        assertEquals(htmlElement, document.findByTag("html").first());
        assertEquals(headElement, document.findByTag("head").first());
        assertEquals(bodyElement, document.findByTag("body").first());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("https://example.com");
        document.body().appendChild(new Element(Tag.valueOf("p"), ""));
        assertEquals("<p></p>", document.outerHtml());
    }

    @Test
    public void testTextSetter() {
        Document document = Document.createShell("https://example.com");
        document.text("Hello World");
        assertEquals("Hello World", document.text());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testOutputSettingsGetter() {
        Document document = Document.createShell("https://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(null, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsSetter() {
        Document document = Document.createShell("https://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        document.outputSettings(outputSettings);
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksModeGetter() {
        Document document = Document.createShell("https://example.com");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
    }

    @Test
    public void testQuirksModeSetter() {
        Document document = Document.createShell("https://example.com");
        document.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
    }

}