
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        
        assertNotNull(document.baseUri());
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        assertNotNull(doc.baseUri());
        assertEquals("#root", doc.nodeName());
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);
        
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.head());
        
        Element head = doc.appendElement("head");
        
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.body());
        
        Element body = doc.appendElement("body");
        
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.title());
        
        Element titleEl = doc.appendElement("title");
        titleEl.text("Test Title");
        
        assertEquals("Test Title", doc.title());
        
        doc.title("New Title");
        
        assertEquals("New Title", doc.title());
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
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        
        Element div1 = doc.createElement("div");
        div1.text("Text 1");
        body.appendChild(div1);
        
        doc.normalise();
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);
        
        Element newBody = doc.body();
        
        assertEquals(body, newBody);
        
        Element div2 = newBody.getElementsByTag("div").first();
        assertNotNull(div2);
        assertEquals("div", div2.tagName());
        assertEquals("http://example.com", div2.baseUri());
        assertEquals("Text 1", div2.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        html.text("Test Html");
        
        assertEquals("Test Html", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        
        Element body = doc.body();
        assertNotNull(body);
        
        body.text("Test Text");
        
        assertEquals("Test Text", doc.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertNotNull(outputSettings);
        assertNotNull(outputSettings.charset());
        assertNotNull(outputSettings.escapeMode());
        
        outputSettings.charset("UTF-16");
        
        assertEquals("UTF-16", outputSettings.charset().name());
    }
}

