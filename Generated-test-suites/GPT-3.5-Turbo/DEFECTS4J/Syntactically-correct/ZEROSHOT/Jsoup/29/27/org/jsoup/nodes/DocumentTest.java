import org.jsoup.nodes.Element;
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
        assertEquals(Tag.html, shell.tagName());
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
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
    public void testcreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document normalisedDocument = document.normalise();
        assertNotNull(normalisedDocument);
    }
    
    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }
    
    @Test
    public void testText() {
        Element body = document.body();
        assertNotNull(body);
        
        document.text("Test Text");
        assertEquals("Test Text", body.text());
    }
    
    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.outputSettings(), clone.outputSettings());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        
        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);
        assertEquals(newOutputSettings, document.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        
        document.quirksMode(Document.QuirksMode.quirks);
        quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.quirks, quirksMode);
    }
}