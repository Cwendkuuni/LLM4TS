import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleWithNoTitleElement() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.head().appendElement("title");
        title.text("Existing Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testTitleWithExistingTitleElementInBody() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.body().appendElement("title");
        title.text("Existing Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        assertNotNull(doc.head().getElementsByTag("title").first());
    }

    @Test
    public void testHeadAppendElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.head().appendElement("meta");
        assertNotNull(element);
        assertEquals("meta", element.tagName());
    }

    @Test
    public void testBodyAppendElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.body().appendElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = Document.createShell("http://example.com");
        Element div = doc.body().appendElement("div");
        div.text("   Text   ");
        doc.normaliseTextNodes(div);
        assertEquals("Text", div.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element body = html.appendElement("body");
        Element head = html.appendElement("head");
        Element head2 = html.appendElement("head");
        assertNotNull(doc.findFirstElementByTagName("head", doc));
        doc.normaliseStructure("head", html);
        assertEquals(head, doc.head());
        assertNull(doc.findFirstElementByTagName("head", doc));
        assertEquals(1, html.getElementsByTag("head").size());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        Element div = doc.body().appendElement("div");
        div.text("Test");
        assertEquals("<html><head></head><body><div>Test</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test");
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://example.com");
        Document clone = doc.clone();
        assertEquals(doc.outerHtml(), clone.outerHtml());
        assertNotSame(doc, clone);
        assertNotSame(doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        doc.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, doc.quirksMode());
    }
}