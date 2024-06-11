import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    private String baseUri;
    
    @Before
    public void setUp() {
        baseUri = "http://example.com";
        document = new Document(baseUri);
    }
    
    @Test
    public void testConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);
        assertNotNull(shell);
        assertEquals("#root", shell.nodeName());
        assertEquals(baseUri, shell.baseUri());
        assertEquals("html", shell.children().first().nodeName());
        assertEquals("head", shell.head().nodeName());
        assertEquals("body", shell.body().nodeName());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
        
        Element newHead = document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals(newHead, head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        
        Element newBody = document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals(newBody, body);
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
        
        Element titleElement = document.appendElement("title");
        titleElement.text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
        
        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
        
        document.head().removeChild(titleElement);
        title = document.title();
        assertEquals("", title);
    }
    
    @Test
    public void testNormalise() {
        Document shell = Document.createShell(baseUri);
        Element html = shell.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        head.appendChild(new Element("title").text("Test Title"));
        head.appendChild(new Element("style").text("body { font-size: 12px; }"));
        head.prependElement("base");
        
        Element body = html.getElementsByTag("body").first();
        body.append("<p>Testing paragraph</p>");
        body.append("<div>Testing div</div>");
        body.append("<span>Testing span</span>");
        
        shell.normalise();
        
        assertEquals(2, shell.getElementsByTag("html").size());
        assertEquals(1, shell.getElementsByTag("head").size());
        assertEquals(1, shell.getElementsByTag("body").size());
        
        assertNull(shell.getElementsByTag("html").get(0).parent());
        assertEquals(shell.getElementsByTag("html").get(1), shell.head().parent());
        assertEquals(shell.getElementsByTag("html").get(1), shell.body().parent());
        
        assertNull(shell.getElementsByTag("head").get(0).parent());
        assertNull(shell.getElementsByTag("head").get(0).getElementsByTag("title").first().parent());
        assertNull(shell.getElementsByTag("head").get(0).getElementsByTag("style").first().parent());
        assertNull(shell.getElementsByTag("head").get(0).getElementsByTag("base").first().parent());
        assertEquals(shell.getElementsByTag("head").get(0), shell.getElementsByTag("title").first().parent());
        assertEquals(shell.getElementsByTag("head").get(0), shell.getElementsByTag("style").first().parent());
        assertEquals(shell.getElementsByTag("head").get(0), shell.getElementsByTag("base").first().parent());
        
        assertNull(shell.getElementsByTag("body").get(0).parent());
        assertEquals(shell.getElementsByTag("body").get(0), shell.getElementsByTag("p").first().parent());
        assertEquals(shell.getElementsByTag("body").get(0), shell.getElementsByTag("div").first().parent());
        assertEquals(shell.getElementsByTag("body").get(0), shell.getElementsByTag("span").first().parent());
    }
    
    @Test
    public void testOuterHtml() {
        document.append("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>");
        String outerHtml = document.outerHtml();
        
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", outerHtml);
    }
    
    @Test
    public void testText() {
        document.append("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>");
        document.text("New Text");
        
        assertEquals("New Text", document.body().text());
        assertEquals("Test Title", document.getElementsByTag("title").first().text());
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals("#root", clone.nodeName());
        assertEquals(baseUri, clone.baseUri());
        assertNotSame(document.outputSettings(), clone.outputSettings());
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
    }
    
    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        
        Document.QuirksMode quirksMode = Document.QuirksMode.limitedQuirks;
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }
    
}