import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        
        assertNotNull(shell);
        assertEquals("<html><head></head><body></body></html>", shell.outerHtml());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        
        assertNotNull(title);
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        document.title("Test Title");
        
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document normalisedDoc = document.normalise();
        
        assertNotNull(normalisedDoc);
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertFalse(normalisedDoc.select("html").isEmpty());
    }
    
    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        
        assertNotNull(outerHtml);
        assertFalse(outerHtml.contains("<html>"));
        assertFalse(outerHtml.contains("<body>"));
        assertFalse(outerHtml.contains("</html>"));
        assertFalse(outerHtml.contains("</body>"));
    }
    
    @Test
    public void testText() {
        document.text("Test Text");
        
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}