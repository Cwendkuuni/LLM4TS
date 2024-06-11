import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc.createElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc.createElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Title");
        title = doc.title();
        assertEquals("Title", title);
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.createElement("html");
        doc.createElement("head");
        doc.createElement("body");

        doc.normalise();

        Element html = doc.child(0);
        assertEquals("html", html.nodeName());
        Element head = html.child(0);
        assertEquals("head", head.nodeName());
        Element body = html.child(1);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element div = doc.createElement("div");
        div.text("content");
        doc.appendChild(div);

        String html = doc.outerHtml();
        assertEquals("content", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.createElement("body");

        doc.text("content");

        Element body = doc.body();
        assertEquals("content", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertEquals(doc.baseUri(), clone.baseUri());
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());

        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        doc.outputSettings(newOutputSettings);
        assertSame(newOutputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        doc.quirksMode(Document.QuirksMode.limitedQuirks);
        quirksMode = doc.quirksMode();
        assertEquals(Document.QuirksMode.limitedQuirks, quirksMode);
    }
}