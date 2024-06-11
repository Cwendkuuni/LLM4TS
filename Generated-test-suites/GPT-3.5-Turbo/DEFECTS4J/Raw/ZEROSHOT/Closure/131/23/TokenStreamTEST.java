import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    
    private TokenStream tokenStream;
    
    @Before
    public void setUp() {
        tokenStream = new TokenStream();
    }
    
    @Test
    public void testIsKeyword() {
        // Test with valid keywords
        assertTrue(tokenStream.isKeyword("if"));
        assertTrue(tokenStream.isKeyword("in"));
        assertTrue(tokenStream.isKeyword("do"));
        
        // Test with invalid keywords
        assertFalse(tokenStream.isKeyword("invalid"));
        assertFalse(tokenStream.isKeyword("key"));
        assertFalse(tokenStream.isKeyword("words"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        // Test with valid JavaScript identifiers
        assertTrue(tokenStream.isJSIdentifier("valid"));
        assertTrue(tokenStream.isJSIdentifier("$valid"));
        assertTrue(tokenStream.isJSIdentifier("_valid"));
        
        // Test with invalid JavaScript identifiers
        assertFalse(tokenStream.isJSIdentifier("invalid!"));
        assertFalse(tokenStream.isJSIdentifier("in.valid"));
        assertFalse(tokenStream.isJSIdentifier("1invalid"));
    }
}