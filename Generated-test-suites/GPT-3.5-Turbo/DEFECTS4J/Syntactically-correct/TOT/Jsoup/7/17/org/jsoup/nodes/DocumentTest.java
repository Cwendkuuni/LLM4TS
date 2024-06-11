import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DocumentTest {
    
    private static Document document;
    
    @BeforeAll
    public static void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("http://www.example.com");
        assertNotNull(shellDocument);
        assertEquals("http://www.example.com", shellDocument.baseUri());
        assertNotNull(shellDocument.head());
        assertNotNull(shellDocument.body());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
        
        // Edge case: title with leading and trailing whitespace
        String titleWithWhitespace = "  Test Title  ";
        document.title(titleWithWhitespace);
        assertEquals(title, document.title());
        
        // Error scenario: null title
        try {
            document.title(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testCreateElement() {
        String tagName = "a";
        Document.Element element = document.createElement(tagName);
        assertNotNull(element);
        assertEquals(Tag.valueOf(tagName), element.tag());
        assertEquals(document.baseUri(), element.baseUri());
        
        // Edge case: element with invalid tag name
        String invalidTagName = "#notvalid";
        try {
            document.createElement(invalidTagName);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testNormalise() {
        Document.Element html = document.appendElement("html");
        Document.Element head = html.appendElement("head");
        html.appendElement("body");
        
        // Normalisation should move text nodes into the body element
        head.appendText("Head 1");
        head.appendText("Head 2");
        html.appendText("HTML 1");
        html.appendText("HTML 2");
        document.appendText("Document 1");
        document.appendText("Document 2");
        
        document.normalise();
        
        assertEquals(0, head.textNodes().size());
        assertEquals(0, html.textNodes().size());
        assertEquals(1, document.body().textNodes().size());
        
        assertEquals("Head 1Head 2HTML 1HTML 2Document 1Document 2",
                document.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        String html = "<html><head></head><body></body></html>";
        assertEquals(html, document.outerHtml());
    }
    
    @Test
    public void testText() {
        String text = "Test text";
        document.text(text);
        assertEquals(text, document.text());
        
        // Error scenario: null text
        try {
            document.text(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        // Test setting escape mode
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        // Test setting charset
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        
        // Test setting pretty print
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        
        // Test setting indent amount
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }
}