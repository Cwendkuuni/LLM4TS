import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        Element headElement = document.appendElement("head");
        head = document.head();
        assertEquals(headElement, head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element bodyElement = document.appendElement("body");
        body = document.body();
        assertEquals(bodyElement, body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Example Page");
        title = document.title();
        assertEquals("Example Page", title);
    }

    @Test
    public void testText() {
        document.text("This is a test");
        String text = document.body().text();
        assertEquals("This is a test", text);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        doc.normalise();
        Element html = doc.getElementsByTag("html").first();
        assertEquals("head", html.child(0).tagName());
        assertEquals("body", html.child(1).tagName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);

        document.appendElement("div");
        outerHtml = document.outerHtml();
        assertEquals("<div></div>", outerHtml);
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.quirks);
        quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.quirks, quirksMode);
    }
}