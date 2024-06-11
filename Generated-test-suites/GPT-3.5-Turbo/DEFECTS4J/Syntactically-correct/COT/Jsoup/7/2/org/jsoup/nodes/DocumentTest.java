import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://example.com");
        
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("https://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("https://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("https://example.com");
        Element head = document.head();
        
        assertEquals("head", head.tagName());
        assertEquals(document, head.ownerDocument());
    }

    @Test
    public void testBody() {
        Document document = new Document("https://example.com");
        Element body = document.body();
        
        assertEquals("body", body.tagName());
        assertEquals(document, body.ownerDocument());
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        document.createElement("title").text("Page Title");
        
        assertEquals("Page Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("https://example.com");
        document.title("Page Title");
        
        assertEquals("Page Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://example.com");
        Element element = document.createElement("div");
        
        assertEquals("div", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");
        html.appendElement("div").text("This is some text.");

        assertEquals("html", document.normalise().body().parent().tagName());
        assertEquals("div", document.body().child(0).tagName());
        assertEquals("This is some text.", document.body().child(0).text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");
        html.appendElement("div").text("This is some text.");

        assertEquals("<html><head></head><body><div>This is some text.</div></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("https://example.com");
        document.body().text("This is some text.");

        assertEquals("This is some text.", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://example.com");

        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("https://example.com");
        Document.OutputSettings settings = document.outputSettings();

        assertNotNull(settings);
        assertNotNull(settings.escapeMode());
        assertNotNull(settings.charset());
        assertNotNull(settings.prettyPrint());
        assertNotNull(settings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document document = new Document("https://example.com");
        Document.OutputSettings settings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, settings.escapeMode());

        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document document = new Document("https://example.com");
        Document.OutputSettings settings = document.outputSettings();

        assertEquals(Charset.forName("UTF-8"), settings.charset());

        settings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), settings.charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document document = new Document("https://example.com");
        Document.OutputSettings settings = document.outputSettings();

        assertTrue(settings.prettyPrint());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document document = new Document("https://example.com");
        Document.OutputSettings settings = document.outputSettings();

        assertEquals(1, settings.indentAmount());

        settings.indentAmount(4);
        assertEquals(4, settings.indentAmount());
    }

}