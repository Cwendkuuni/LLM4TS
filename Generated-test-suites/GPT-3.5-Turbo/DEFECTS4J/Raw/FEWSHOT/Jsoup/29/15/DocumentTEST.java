// Test for Document class
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;
import org.junit.*;

import java.io.IOException;

public class DocumentTest {

    @Test
    public void testDocumentCreationWithBaseUri() {
        // Test document creation with base Uri
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        // Test createShell method
        String baseUri = "http://www.example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHeadElement() {
        // Test head method
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().nodeName());
    }
    
    @Test
    public void testBodyElement() {
        // Test body method
        Document doc = Jsoup.parse("<html><head></head><body><h1>Hello World!</h1></body></html>");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        // Test title method
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testSetTitle() {
        // Test title method
        Document doc = Jsoup.parse("<html><head><title>Old Title</title></head><body></body></html>");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        // Test createElement method
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        assertNotNull(doc.createElement("div"));
    }
    
    @Test
    public void testNormalise() {
        // Test normalise method
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testOuterHtml() {
        // Test outerHtml method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        assertEquals("<html><head></head><body><div>Hello World!</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        // Test text method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        doc.text("New Text");
        assertEquals("New Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        // Test nodeName method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        // Test clone method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        Document clone = doc.clone();
        assertNotNull(clone);
        assertEquals(doc.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        // Test outputSettings method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }
  
    @Test
    public void testQuirksMode() {
        // Test quirksMode method
        Document doc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        doc.quirksMode(quirksMode);
        assertEquals(quirksMode, doc.quirksMode());
    }
}