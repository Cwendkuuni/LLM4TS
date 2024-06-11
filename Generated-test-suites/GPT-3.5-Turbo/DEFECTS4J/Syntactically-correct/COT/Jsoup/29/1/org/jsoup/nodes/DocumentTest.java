
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {

    // Test case for creating a new, empty Document
    @Test
    public void testCreateEmptyDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }
    
    // Test case for creating a valid, empty shell of a document
    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    // Test case for accessing the document's head element
    @Test
    public void testGetHeadElement() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        assertNotNull(head);
    }
    
    // Test case for accessing the document's body element
    @Test
    public void testGetBodyElement() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();
        assertNotNull(body);
    }
    
    // Test case for getting the string contents of the document's title element
    @Test
    public void testGetTitle() {
        Document document = Document.createShell("http://example.com");
        String title = document.title();
        assertEquals("", title);
        
        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }
    
    // Test case for creating a new element
    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
    }
    
    // Test case for normalizing the document
    @Test
    public void testNormalize() {
        Document document = Document.createShell("http://example.com");
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
    }

    // Test case for getting the outer html of the document
    @Test
    public void testGetOuterHtml() {
        Document document = Document.createShell("http://example.com");
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }
    
    // Test case for setting the text of the document's body
    @Test
    public void testSetText() {
        Document document = Document.createShell("http://example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    // Test case for getting the document's current output settings
    @Test
    public void testGetOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Document document = Document.createShell("http://example.com");
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }
    
    // Test case for setting the document's output settings
    @Test
    public void testSetOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Document document = Document.createShell("http://example.com");
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }
    
    // Test case for getting the document's quirks mode
    @Test
    public void testGetQuirksMode() {
        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        Document document = Document.createShell("http://example.com");
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }
    
    // Test case for setting the document's quirks mode
    @Test
    public void testSetQuirksMode() {
        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        Document document = Document.createShell("http://example.com");
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }
}

