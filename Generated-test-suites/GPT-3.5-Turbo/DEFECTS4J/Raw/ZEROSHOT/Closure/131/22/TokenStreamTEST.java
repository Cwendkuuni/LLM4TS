import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenStreamTest {
    
    private TokenStream tokenStream;
    
    @Before
    public void setUp() {
        tokenStream = new TokenStream();
    }
    
    @Test
    public void testIsKeyword() {
        // Test cases for 2 characters keywords
        assertTrue(tokenStream.isKeyword("if"));
        assertTrue(tokenStream.isKeyword("in"));
        assertTrue(tokenStream.isKeyword("do"));
        assertFalse(tokenStream.isKeyword("id"));
        
        // Test cases for 3 characters keywords
        assertTrue(tokenStream.isKeyword("for"));
        assertTrue(tokenStream.isKeyword("new"));
        assertFalse(tokenStream.isKeyword("foo"));
        
        // Test cases for 4 characters keywords
        assertTrue(tokenStream.isKeyword("byte"));
        assertTrue(tokenStream.isKeyword("case"));
        assertFalse(tokenStream.isKeyword("bar"));
        
        // Test cases for 5 characters keywords
        assertTrue(tokenStream.isKeyword("class"));
        assertTrue(tokenStream.isKeyword("false"));
        assertFalse(tokenStream.isKeyword("baz"));
        
        // Test cases for 6 characters keywords
        assertTrue(tokenStream.isKeyword("delete"));
        assertTrue(tokenStream.isKeyword("native"));
        assertFalse(tokenStream.isKeyword("hello"));
        
        // Test cases for 7 characters keywords
        assertTrue(tokenStream.isKeyword("package"));
        assertTrue(tokenStream.isKeyword("default"));
        assertFalse(tokenStream.isKeyword("world"));
        
        // Test cases for 8 characters keywords
        assertTrue(tokenStream.isKeyword("continue"));
        assertTrue(tokenStream.isKeyword("function"));
        assertFalse(tokenStream.isKeyword("fooBar"));
        
        // Test cases for 9 characters keywords
        assertTrue(tokenStream.isKeyword("interface"));
        assertTrue(tokenStream.isKeyword("protected"));
        assertFalse(tokenStream.isKeyword("barFoo"));
        
        // Test cases for 10 characters keywords
        assertTrue(tokenStream.isKeyword("implements"));
        assertTrue(tokenStream.isKeyword("instanceof"));
        assertFalse(tokenStream.isKeyword("fooBarBaz"));
        
        // Test case for 12 characters keyword
        assertTrue(tokenStream.isKeyword("synchronized"));
        assertFalse(tokenStream.isKeyword("bazBarFoo"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(tokenStream.isJSIdentifier("foo"));
        assertTrue(tokenStream.isJSIdentifier("bar"));
        assertFalse(tokenStream.isJSIdentifier("foo.bar"));
        assertFalse(tokenStream.isJSIdentifier("foo-bar"));
        assertFalse(tokenStream.isJSIdentifier("foo_bar"));
        assertFalse(tokenStream.isJSIdentifier("123"));
        assertFalse(tokenStream.isJSIdentifier("$foo"));
        assertFalse(tokenStream.isJSIdentifier("@bar"));
    }
}