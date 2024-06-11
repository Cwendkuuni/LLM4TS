
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
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
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
        
        doc.appendElement("title").text("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        try {
            doc.title(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }
    
    @Test
    public void testTitleExistingElement() {
        Document doc = new Document("http://example.com");
        doc.appendElement("title").text("Old Title");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
    }
    
    @Test
    public void testTitleNewElement() {
        Document doc = new Document("http://example.com");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        doc.normalise();
        
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(html, doc.getElementsByTag("html").first());
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.createElement("p").text("Test paragraph");
        assertEquals("<p>Test paragraph</p>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("p").text("Test paragraph");
        assertEquals("Test paragraph", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
    
    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertNotSame(doc.outputSettings(), clone.outputSettings());
        assertEquals(doc.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals(doc.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals(doc.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals(doc.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        assertSame(outputSettings, doc.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        doc.quirksMode(quirksMode);
        assertSame(quirksMode, doc.quirksMode());
    }
}
