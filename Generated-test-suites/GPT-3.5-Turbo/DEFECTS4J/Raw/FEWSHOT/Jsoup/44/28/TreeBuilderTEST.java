import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        // Test case when input and baseUri are not null
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        treeBuilder.initialiseParse("input", "baseUri", null);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.errors);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals("baseUri", treeBuilder.baseUri);
        
        // Test case when input is null
        try {
            treeBuilder.initialiseParse(null, "baseUri", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String input must not be null", e.getMessage());
        }
        
        // Test case when baseUri is null
        try {
            treeBuilder.initialiseParse("input", null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParse() {
        // Test case when error tracking is not enabled
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Document doc = treeBuilder.parse("input", "baseUri");
        assertNotNull(doc);
        assertNull(treeBuilder.errors);
        
        // Test case when error tracking is enabled
        ParseErrorList errors = new ParseErrorList();
        doc = treeBuilder.parse("input", "baseUri", errors);
        assertNotNull(doc);
        assertNotNull(treeBuilder.errors);
        assertSame(errors, treeBuilder.errors);
    }
    
    @Test
    public void testRunParser() {
        // Test case when token type is not EOF
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Tokeniser.Token token1 = new Tokeniser.Token(Tokeniser.TokenType.StartTag);
        Tokeniser.Token token2 = new Tokeniser.Token(Tokeniser.TokenType.EndTag);
        token1.reset();
        token2.reset();
        Tokeniser.Token eofToken = new Tokeniser.Token(Tokeniser.TokenType.EOF);
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.addToken(token1);
        tokeniser.addToken(token2);
        tokeniser.addToken(eofToken);
        treeBuilder.tokeniser = tokeniser;
        
        boolean result = treeBuilder.runParser();
        assertTrue(result);
        assertTrue(token1.processed);
        assertTrue(token2.processed);
        
        // Test case when token type is EOF
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Tokeniser.Token eofToken = new Tokeniser.Token(Tokeniser.TokenType.EOF);
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.addToken(eofToken);
        treeBuilder.tokeniser = tokeniser;
        
        boolean result = treeBuilder.runParser();
        assertTrue(result);
    }
    
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        
        // Test case when process(start) returns true
        Token.StartTag startTag = new Token.StartTag();
        treeBuilder.start = startTag;
        
        boolean result = treeBuilder.processStartTag("name");
        assertTrue(result);
        assertEquals("name", startTag.name);
        
        // Test case when process(start) returns false
        startTag = new Token.StartTag();
        treeBuilder.start = startTag;
        
        boolean result = treeBuilder.processStartTag("");
        assertFalse(result);
        assertEquals("", startTag.name);
    }
    
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        
        // Test case when process(end) returns true
        Token.EndTag endTag = new Token.EndTag();
        treeBuilder.end = endTag;
        
        boolean result = treeBuilder.processEndTag("name");
        assertTrue(result);
        assertEquals("name", endTag.name);
        
        // Test case when process(end) returns false
        endTag = new Token.EndTag();
        treeBuilder.end = endTag;
        
        boolean result = treeBuilder.processEndTag("");
        assertFalse(result);
        assertEquals("", endTag.name);
    }
    
    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        
        // Test case when stack is empty
        Element result = treeBuilder.currentElement();
        assertNull(result);
        
        // Test case when stack is not empty
        Element element1 = new Element();
        Element element2 = new Element();
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        result = treeBuilder.currentElement();
        assertSame(element2, result);
    }
}