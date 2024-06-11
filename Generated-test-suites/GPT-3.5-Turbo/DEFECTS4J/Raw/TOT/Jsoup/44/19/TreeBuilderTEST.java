import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = treeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("html", document.child(0).tagName());
        assertEquals("head", document.child(0).child(0).tagName());
        assertEquals("title", document.child(0).child(0).child(0).tagName());
        assertEquals("Test", document.child(0).child(0).child(0).ownText());
        assertEquals("body", document.child(0).child(1).tagName());
        assertEquals("h1", document.child(0).child(1).child(0).tagName());
        assertEquals("Hello World", document.child(0).child(1).child(0).ownText());
    }
    
    @Test
    public void testProcessStartTag() {
        assertFalse(treeBuilder.processStartTag("test"));
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("id", "testId");
        attrs.put("class", "testClass");
        
        assertFalse(treeBuilder.processStartTag("test", attrs));
    }
    
    @Test
    public void testProcessEndTag() {
        assertFalse(treeBuilder.processEndTag("test"));
    }
    
    @Test
    public void testCurrentElementWithEmptyStack() {
        assertNull(treeBuilder.currentElement());
    }
    
    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        
        assertEquals(element, treeBuilder.currentElement());
    }
}