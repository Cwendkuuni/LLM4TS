import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {
    
    private Document testDocument;
    
    @Before
    public void setUp() {
        testDocument = Document.createShell("https://example.com");
    }
    
    @Test
    public void testCreateShell() {
        assertNotNull(testDocument);
        assertEquals("https://example.com", testDocument.baseUri());
        assertEquals("html", testDocument.nodeName());
    }
    
    @Test
    public void testHead() {
        Element headElement = testDocument.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }
    
    @Test
    public void testBody() {
        Element bodyElement = testDocument.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", testDocument.title());
        testDocument.title("Test Title");
        assertEquals("Test Title", testDocument.title());
    }
    
    @Test
    public void testCreateElement() {
        Element testElement = testDocument.createElement("div");
        assertEquals("div", testElement.tagName());
        assertEquals("https://example.com", testElement.baseUri());
    }
    
    @Test
    public void testNormalise() {
        TextNode textNode = new TextNode("Test text", "");
        testDocument.appendChild(textNode);
        testDocument.normalise();
        Element bodyElement = testDocument.body();
        assertEquals("Test text", bodyElement.text());
    }
    
    @Test
    public void testText() {
        testDocument.text("Test text content");
        assertEquals("Test text content", testDocument.body().text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = testDocument.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
    }
    
}