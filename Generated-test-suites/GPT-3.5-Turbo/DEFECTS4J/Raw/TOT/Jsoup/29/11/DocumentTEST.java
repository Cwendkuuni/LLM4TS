import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }
    
    @Test
    public void testConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
        
        Element headElement = document.appendElement("head");
        head = document.head();
        assertEquals(headElement, head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        
        Element bodyElement = document.appendElement("body");
        body = document.body();
        assertEquals(bodyElement, body);
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
        
        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertEquals("#root", normalizedDoc.nodeName());
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
    }
    
    @Test
    public void testNormaliseTextNodes() {
        Element element = document.appendElement("div");
        TextNode textNode1 = new TextNode("Text 1", "");
        TextNode textNode2 = new TextNode("Text 2", "");
        element.appendChild(textNode1);
        element.appendChild(textNode2);
        
        document.normaliseTextNodes(element);
        
        assertEquals("Text 1", element.childNodes().get(0).outerHtml());
        assertEquals(" ", element.childNodes().get(1).outerHtml());
        assertEquals("Text 2", element.childNodes().get(2).outerHtml());
    }
    
    @Test
    public void testNormaliseStructure() {
        Element html = document.appendElement("html");
        Element body = html.appendElement("body");
        Element head1 = html.appendElement("head");
        Element head2 = html.appendElement("head");
        Element child1 = new Element(Tag.valueOf("div"), "");
        Element child2 = new Element(Tag.valueOf("div"), "");
        Element child3 = new Element(Tag.valueOf("div"), "");
        head2.appendChild(child1);
        head2.appendChild(child2);
        head2.appendChild(child3);
        
        document.normaliseStructure("head", html);
        
        assertNull(document.getElementsByTag("head").get(1));
        assertNotNull(document.getElementsByTag("head").get(0));
        assertEquals(3, document.getElementsByTag("head").get(0).childrenSize());
    }
    
    @Test
    public void testOuterHtml() {
        document.appendElement("div");
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.startsWith("<div"));
        assertTrue(outerHtml.endsWith("</div>"));
    }
    
    @Test
    public void testText() {
        document.appendElement("body");
        document.text("Test Text");
        
        Element body = document.body();
        assertEquals("Test Text", body.text());
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals("https://www.example.com", clone.baseUri());
        assertEquals("#root", clone.nodeName());
        
        assertNotSame(document.outputSettings(), clone.outputSettings());
        assertEquals(document.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals(document.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals(document.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        
        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);
        
        assertSame(newOutputSettings, document.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull(quirksMode);
        
        document.quirksMode(Document.QuirksMode.quirks);
        
        assertSame(Document.QuirksMode.quirks, document.quirksMode());
    }
    
}