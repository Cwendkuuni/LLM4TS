import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Typical use case
        assertTrue(TokenStream.isKeyword("while"));
        
        // Edge cases
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("final"));
        
        // Error scenarios
        assertFalse(TokenStream.isKeyword("if-else"));
        assertFalse(TokenStream.isKeyword("nulll"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        // Typical use case
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        
        // Edge cases
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("variable_1"));
        assertTrue(TokenStream.isJSIdentifier("_privateVar"));
        
        // Error scenarios
        assertFalse(TokenStream.isJSIdentifier("1stVar"));
        assertFalse(TokenStream.isJSIdentifier("#var"));
        assertFalse(TokenStream.isJSIdentifier("my Var"));
    }
}