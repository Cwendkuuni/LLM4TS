
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        
        assertNotNull(doc);
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals(baseUri, doc.baseUri());
        
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
        
        Element html = doc.appendElement("html");
        html.appendElement("head");
        
        head = doc.head();
        assertNotNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        
        Element body = doc.body();
        
        assertNull(body);
        
        Element html = doc.appendElement("html");
        html.appendElement("body");
        
        body = doc.body();
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        String title = doc.title();
        
        assertEquals("", title);
        
        Element html = doc.appendElement("html");
        html.appendElement("head").appendElement("title").text("Test Title");
        
        title = doc.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        
        doc.title("Test Title");
        
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());
        
        doc.title("New Title");
        
        assertEquals("New Title", titleEl.text());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        
        Element div = doc.createElement("div");
        
        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        
        Element html = doc.getElementsByTag("html").first();
        html.appendElement("head");
        html.appendElement("body").text("Test");
        
        Element head = html.getElementsByTag("head").first();
        head.appendText("Test");
        
        doc.normalise();
        
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals(2, body.childNodes().size());
        
        Node firstChild = body.childNodes().get(0);
        assertTrue(firstChild instanceof TextNode);
        assertEquals(" ", ((TextNode) firstChild).text());
        
        Node lastChild = body.childNodes().get(1);
        assertTrue(lastChild instanceof TextNode);
        assertEquals("Test", ((TextNode) lastChild).text());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        
        doc.appendElement("div").text("Test");
        
        String outerHtml = doc.outerHtml();
        
        assertEquals("<div>Test</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        
        doc.text("Test");
        
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Test", body.text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        
        String nodeName = doc.nodeName();
        
        assertEquals("#document", nodeName);
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }
    
    @Test
    public void testOutputSettingsCharsetString() {
        Document doc = new Document("http://example.com");
        
        doc.outputSettings().charset("ISO-8859-1");
        
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
        
        doc.outputSettings().charset("UTF-8");
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());
    }
}
