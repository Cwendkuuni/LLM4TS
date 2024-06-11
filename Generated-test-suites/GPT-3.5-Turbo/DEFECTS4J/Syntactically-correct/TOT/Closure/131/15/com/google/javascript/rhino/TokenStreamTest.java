import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Typical use cases
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        
        // Edge cases
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("true"));
        
        // Error scenarios
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        // Typical use cases
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("test123"));
        
        // Edge cases
        assertFalse(TokenStream.isJSIdentifier(""));
        
        // Error scenarios
        assertFalse(TokenStream.isJSIdentifier("123test"));
        assertFalse(TokenStream.isJSIdentifier("abc!"));
    }
}