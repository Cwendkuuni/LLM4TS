import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse_NullInput() {
        String input = null;
        String baseUri = "https://www.example.com";
        ParseErrorList errors = null;
        
        try {
             TreeBuilder treeBuilder = new TreeBuilder();
             treeBuilder.initialiseParse(input, baseUri, errors);
             fail("Expected NullPointerException");
        } catch(NullPointerException e) {
             assertEquals("String input must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testInitialiseParse_NullBaseUri() {
        String input = "<html></html>";
        String baseUri = null;
        ParseErrorList errors = null;
        
        try {
             TreeBuilder treeBuilder = new TreeBuilder();
             treeBuilder.initialiseParse(input, baseUri, errors);
             fail("Expected NullPointerException");
        } catch(NullPointerException e) {
             assertEquals("BaseURI must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        
        TreeBuilder treeBuilder = new TreeBuilder();
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.tagName());
        
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        
        Element title = head.selectFirst("title");
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());
        
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Heading", h1.text());
        
        Element p = body.selectFirst("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
        assertEquals("Paragraph", p.text());
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        boolean result = treeBuilder.processStartTag("html");
        assertTrue(result);
        
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("html", currentElement.tagName());
        
        result = treeBuilder.processStartTag("head");
        assertTrue(result);
        
        currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("head", currentElement.tagName());
    }
    
    @Test
    public void testProcessStartTag_WithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Attributes attrs = new Attributes();
        attrs.put("class", "test");
        
        boolean result = treeBuilder.processStartTag("body", attrs);
        assertTrue(result);
        
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("body", currentElement.tagName());
        
        assertEquals("test", currentElement.className());
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        boolean result = treeBuilder.processStartTag("html");
        assertTrue(result);
        
        result = treeBuilder.processStartTag("head");
        assertTrue(result);
        
        result = treeBuilder.processEndTag("head");
        assertTrue(result);
        
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("html", currentElement.tagName());
        
        result = treeBuilder.processEndTag("html");
        assertTrue(result);
        
        currentElement = treeBuilder.currentElement();
        assertNull(currentElement);
    }
}