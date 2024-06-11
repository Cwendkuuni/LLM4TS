import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertEquals("html", shell.nodeName());
        Element head = shell.head();
        assertEquals("head", head.nodeName());
        Element body = shell.body();
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        Element titleElement = document.getElementsByTag("title").first();
        assertNull(titleElement);
        
        document.title("Page Title");
        assertEquals("Page Title", document.title());
        
        titleElement = document.getElementsByTag("title").first();
        assertEquals("title", titleElement.nodeName());
        assertEquals("Page Title", titleElement.text());
    }
    
    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document normalisedDoc = document.normalise();
        
        Element htmlElement = normalisedDoc.getElementsByTag("html").first();
        assertEquals("html", htmlElement.nodeName());
        
        Element headElement = normalisedDoc.getElementsByTag("head").first();
        assertEquals("head", headElement.nodeName());
        
        Element bodyElement = normalisedDoc.getElementsByTag("body").first();
        assertEquals("body", bodyElement.nodeName());
        
        assertEquals(0, document.childNodes().size());
    }
    
    @Test
    public void testOuterHtml() {
        Element div = document.appendElement("div");
        div.text("This is a div element.");
        
        String expectedHtml = "<div>This is a div element.</div>";
        assertEquals(expectedHtml, document.outerHtml());
    }
    
    @Test
    public void testText() {
        Element body = document.body();
        body.text("This is some text.");
        
        assertEquals("This is some text.", body.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}