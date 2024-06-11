import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");

        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");

        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.getElementsByTag("html").first().tagName());
        assertEquals("head", doc.getElementsByTag("head").first().tagName());
        assertEquals("body", doc.getElementsByTag("body").first().tagName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        
        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();
        
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        
        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();
        
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        
        assertEquals("", title);
        
        doc.title("Example Page");
        title = doc.title();
        
        assertEquals("Example Page", title);
    }
    
    @Test
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        try {
            doc.title(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    @Test
    public void testTitleWithExistingTitleElement() {
        Document doc = new Document("http://example.com");
        doc.appendElement("head").appendElement("title").text("Existing Title");
        
        doc.title("New Title");
        String title = doc.title();
        
        assertEquals("New Title", title);
    }
    
    @Test
    public void testTitleWithoutTitleElement() {
        Document doc = new Document("http://example.com");
        doc.head().remove();
        
        doc.title("New Title");
        String title = doc.title();
        
        assertEquals("New Title", title);
    }
    
    @Test
    public void testTitleEmptyElement() {
        Document doc = new Document("http://example.com");
        doc.title("");
        String title = doc.title();
        
        assertEquals("", title);
    }
    
    @Test
    public void testTitleWithWhiteSpaceElement() {
        Document doc = new Document("http://example.com");
        doc.title("   ");
        String title = doc.title();
        
        assertEquals("", title);
    }
    
    @Test
    public void testTitleWithNonBreakingSpaceElement() {
        Document doc = new Document("http://example.com");
        doc.title("\u00A0");
        String title = doc.title();
        
        assertEquals("", title);
    }
    
    @Test
    public void testTitleWithTextElements() {
        Document doc = new Document("http://example.com");
        doc.appendElement("title").text("Existing Title");
        
        doc.title("New Title");
        String title = doc.title();
        
        assertEquals("New Title", title);
    }
    
    @Test
    public void testTitleWithChildElements() {
        Document doc = new Document("http://example.com");
        Element titleEl = doc.appendElement("title");
        titleEl.appendElement("span").text("New");
        titleEl.appendElement("span").text("Title");
        
        doc.title("Updated Title");
        String title = doc.title();
        
        assertEquals("Updated Title", title);
    }
    
    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        
        Element a = doc.createElement("a");
        assertEquals("a", a.tagName());
        
        Element div = doc.createElement("div");
        assertEquals("div", div.tagName());
        
        Element span = doc.createElement("span");
        assertEquals("span", span.tagName());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        
        doc.normalise();
        
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.getElementsByTag("html").first());
    }
    
    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");
        body.appendText(" Text Node 1 ");
        body.appendElement("span").text(" Span ").appendText("Text Node 2");
        body.appendText(" Text Node 3 ");
        
        doc.normalise();
        
        assertEquals(1, body.childNodes().size());
        assertTrue(body.childNodes().get(0) instanceof Element);
        assertEquals("span", body.childNodes().get(0).nodeName());
        assertTrue(body.childNodes().get(0).childNodes().get(0) instanceof TextNode);
        assertEquals("Text Node 1", body.childNodes().get(0).childNodes().get(0).outerHtml());
        assertTrue(body.childNodes().get(0).childNodes().get(1) instanceof TextNode);
        assertEquals(" Span ", body.childNodes().get(0).childNodes().get(1).outerHtml());
        assertTrue(body.childNodes().get(0).childNodes().get(2) instanceof TextNode);
        assertEquals("Text Node 2", body.childNodes().get(0).childNodes().get(2).outerHtml());
        assertTrue(body.childNodes().get(0).childNodes().get(3) instanceof TextNode);
        assertEquals(" Text Node 3 ", body.childNodes().get(0).childNodes().get(3).outerHtml());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        
        doc.text("Example Text");
        
        assertEquals("Example Text", doc.body().text());
    }
    
    @Test
    public void testTextWithEncoding() {
        Document doc = new Document("http://example.com");
        doc.outputSettings().charset("ISO-8859-1");
        
        doc.text("\u00A9");
        
        assertEquals("\u00A9", doc.body().text());
    }
    
    @Test
    public void testTextNull() {
        Document doc = new Document("http://example.com");
        
        try {
            doc.text(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");

        assertNotNull(doc.outputSettings());
        assertEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());
        assertTrue(doc.outputSettings().prettyPrint());
        assertEquals(1, doc.outputSettings().indentAmount());
    }
}