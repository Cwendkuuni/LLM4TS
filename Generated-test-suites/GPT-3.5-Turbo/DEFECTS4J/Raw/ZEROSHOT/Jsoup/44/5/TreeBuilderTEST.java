import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        treeBuilder.initialiseParse(input, baseUri, null);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
    }
    
    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        Document document = treeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testProcess() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return true;
            }
        };
        
        Token token = new Token(Token.TokenType.StartTag);
        assertTrue(treeBuilder.process(token));
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return true;
            }
        };
        
        String name = "div";
        assertTrue(treeBuilder.processStartTag(name));
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return true;
            }
        };
        
        String name = "div";
        Attributes attrs = new Attributes();
        assertTrue(treeBuilder.processStartTag(name, attrs));
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return true;
            }
        };
        
        String name = "div";
        assertTrue(treeBuilder.processEndTag(name));
    }
    
    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        
        Element element = treeBuilder.currentElement();
        assertNull(element);
        
        treeBuilder.stack.add(new Element("div"));
        element = treeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
}