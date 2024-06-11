import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }
    
    @Test
    public void testDocumentConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("https://example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertEquals("https://example.com", shell.baseUri());
        
        Element html = shell.getElementsByTag("html").first();
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
        
        Element headElement = document.createElement("head");
        document.appendChild(headElement);
        
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        
        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);
        
        body = document.head();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitleWithTitleElementExists() {
        Element titleElement = document.createElement("title");
        titleElement.text("Test Title");
        document.head().appendChild(titleElement);
        
        String title = document.title();
        assertNotNull(title);
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testTitleWithoutTitleElement() {
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitleWithTitleElementExists() {
        Element titleElement = document.createElement("title");
        titleElement.text("Test Title");
        document.head().appendChild(titleElement);
        
        document.title("New Title");
        assertEquals("New Title", titleElement.text());
    }
    
    @Test
    public void testSetTitleWithoutTitleElement() {
        document.title("New Title");
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        document.appendElement("head");
        
        assertEquals(0, document.select("html").size());
        assertEquals(0, document.select("body").size());
        
        document.normalise();
        
        assertEquals(1, document.select("html").size());
        assertEquals(1, document.select("head").size());
        assertEquals(1, document.select("body").size());
    }
    
    @Test
    public void testOuterHtml() {
        Element div = document.createElement("div");
        div.text("Test");
        
        document.appendChild(div);
        
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("<div>Test</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        Element body = document.createElement("body");
        document.appendChild(body);
        
        document.text("Test");
        
        assertEquals("Test", body.text());
    }
    
    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertNotNull(nodeName);
        assertEquals("#document", nodeName);
    }
}