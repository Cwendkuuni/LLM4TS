import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document doc;
    
    @Before
    public void setUp() {
        doc = new Document("https://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("https://example.com");
        
        assertNotNull(shellDoc);
        assertEquals("html", shellDoc.childNodes.get(0).nodeName());
        assertEquals("head", shellDoc.head().nodeName());
        assertEquals("body", shellDoc.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", doc.title());
        
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Element elem = doc.createElement("a");
        
        assertNotNull(elem);
        assertEquals("a", elem.nodeName());
        assertEquals("https://example.com", elem.baseUri());
    }
    
    @Test
    public void testNormalise() {
        doc.appendElement("span").text("Test Span");
        doc.normalise();
        
        assertEquals("Test Span", doc.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
         doc.title("Test Title");
         doc.text("Test Body");
        
         assertEquals("Test Body", doc.body().text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertNotNull(outputSettings);
        assertSame(Document.OutputSettings.escapeMode.base, outputSettings.escapeMode());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        assertEquals("UTF-8", outputSettings.charset().displayName());
    }
}