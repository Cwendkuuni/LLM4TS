import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.head());
        
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Example Title");
        
        assertEquals(head, doc.head());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.body());
        
        Element body = doc.appendElement("body");
        Element div = body.appendElement("div");
        
        assertEquals(body, doc.body());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.title());
        
        doc.title("Example Title");
        
        assertEquals("Example Title", doc.title());
    }
    
    @Test
    public void testTitle_noTitleElement() {
        Document doc = new Document("http://example.com");
        
        Element head = doc.appendElement("head");
        
        doc.title("Example Title");
        
        assertEquals("Example Title", doc.title());
        assertNotNull(doc.getElementsByTag("title").first());
        assertEquals("Example Title", doc.getElementsByTag("title").first().text());
    }
    
    @Test
    public void testTitle_existingTitleElement() {
        Document doc = new Document("http://example.com");
        
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Previous Title");
        
        doc.title("Example Title");
        
        assertEquals("Example Title", doc.title());
        assertNotNull(doc.getElementsByTag("title").first());
        assertEquals("Example Title", doc.getElementsByTag("title").first().text());
    }
    
    @Test
    public void testTitle_nullTitle() {
        Document doc = new Document("http://example.com");
        
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Previous Title");
        
        assertThrows(IllegalArgumentException.class, () -> {
            doc.title(null);
        });
        
        assertEquals("Previous Title", doc.title());
    }
    
    @Test
    public void testTitle_blankTitle() {
        Document doc = new Document("http://example.com");
        
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Previous Title");
        
        doc.title("");
        
        assertEquals("", doc.title());
        assertNotNull(doc.getElementsByTag("title").first());
        assertTrue(doc.getElementsByTag("title").first().text().isEmpty());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        
        Element element = doc.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        
        Element html = doc.appendElement("html");
        html.appendElement("head");
        
        Element body = html.appendElement("body");
        body.appendText("Hello ");
        
        doc.normalise();
        
        assertEquals("Hello ", doc.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        
        doc.text("Hello");
        
        assertEquals("Hello", doc.body().text());
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
        assertSame(doc.outputSettings(), clone.outputSettings());
        assertSame(doc.outputSettings().charset(), clone.outputSettings().charset());
        assertNotSame(doc.outputSettings().charsetEncoder(), clone.outputSettings().charsetEncoder());
        
        // Update charset of clone
        clone.outputSettings().charset("ISO-8859-1");
        
        assertNotSame(doc.outputSettings().charset(), clone.outputSettings().charset());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.extended);
        settings.charset("ISO-8859-1");
        
        doc.outputSettings(settings);
        
        assertSame(settings, doc.outputSettings());
        assertSame(settings.escapeMode(), doc.outputSettings().escapeMode());
        assertSame(settings.charset(), doc.outputSettings().charset());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        
        assertEquals(Document.QuirksMode.noQuirks, doc.getQuirksMode());
        
        doc.setQuirksMode(Document.QuirksMode.quirks);
        
        assertEquals(Document.QuirksMode.quirks, doc.getQuirksMode());
    }
}