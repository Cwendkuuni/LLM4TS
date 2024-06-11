import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertEquals(doc.nodeName(), "#root");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertEquals(doc.childNodes().size(), 1);
        assertTrue(doc.outerHtml().contains("<html><head></head><body></body></html>"));
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals(head.tagName(), "head");
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(body.tagName(), "body");
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        String title = doc.title();
        assertNotNull(title);
        assertEquals(title, "");
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://www.example.com");
        doc.title("Example Title");
        String title = doc.title();
        assertNotNull(title);
        assertEquals(title, "Example Title");
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element elem = doc.createElement("div");
        assertNotNull(elem);
        assertEquals(elem.tagName(), "div");
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Example Text");
        Element body = doc.body();
        assertNotNull(body.text());
        assertEquals(body.text(), "Example Text");
    }

    @Test
    public void testClone() {
        Document doc = new Document("https://www.example.com");
        Document clone = doc.clone();
        assertNotNull(clone);
        assertEquals(clone.baseUri(), doc.baseUri());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(outputSettings.prettyPrint(), true);
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("https://www.example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(quirksMode, Document.QuirksMode.noQuirks);
    }
}