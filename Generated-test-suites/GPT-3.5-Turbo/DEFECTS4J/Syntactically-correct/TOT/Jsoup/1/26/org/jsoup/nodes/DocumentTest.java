import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {

    private Document doc;
    
    @Before
    public void setup() {
        doc = Document.createShell("https://example.com");
    }
    
    @Test
    public void testCreateShell() {
        // Typical use case
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertNotNull(shell.html());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.getElementsByTag("html").first().tagName());
        assertEquals("head", shell.getElementsByTag("head").first().tagName());
        assertEquals("body", shell.getElementsByTag("body").first().tagName());
        
        // Error scenario
        try {
            Document shellNullUri = Document.createShell(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testHead() {
        // Typical use case
        Element head = doc.head();
        assertNull(head);
        
        // Edge case - with head element
        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        // Typical use case
        Element body = doc.body();
        assertNull(body);
        
        // Edge case - with body element
        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        // Typical use case - title element exists
        doc.head().appendElement("title").text("Example Title");
        String title = doc.title();
        assertEquals("Example Title", title);
        
        // Edge case - title element does not exist
        title = doc.title();
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        // Typical use case - title element exists
        doc.head().appendElement("title").text("Old Title");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
        
        // Typical use case - title element does not exist
        doc.head().getElementsByTag("title").remove();
        doc.title("New Title");
        title = doc.title();
        assertEquals("New Title", title);
        
        // Edge case - title is null
        try {
            doc.title(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testCreateElement() {
        // Typical use case
        Element a = doc.createElement("a");
        assertNotNull(a);
        assertEquals("a", a.tagName());
        assertEquals("https://example.com", a.baseUri());
        
        // Edge case - tagName is null
        try {
            Element nullTag = doc.createElement(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("tagName must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testNormalise() {
        // Typical use case - html, head, and body elements all exist
        doc.normalise();
        assertEquals("html", doc.getElementsByTag("html").first().tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        
        // Edge case - only html element exists
        doc.head().remove();
        doc.body().remove();
        doc.normalise();
        assertEquals("html", doc.getElementsByTag("html").first().tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        
        // Edge case - html element does not exist
        doc.getElementsByTag("html").remove();
        doc.normalise();
        assertEquals("html", doc.getElementsByTag("html").first().tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        // Typical use case
        doc.appendElement("div").text("Example Text");
        String outerHtml = doc.outerHtml();
        assertEquals("<div>Example Text</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        // Typical use case
        Element div = doc.createElement("div");
        doc.body().appendChild(div);
        doc.text("Example Text");
        String text = doc.body().text();
        assertEquals("Example Text", text);
    }
    
    @Test
    public void testNodeName() {
        // Typical use case
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }
    
}