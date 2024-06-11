import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertFalse(TokenStream.isKeyword(""));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertFalse(TokenStream.isKeyword("foo"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("delete"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("!invalid"));
        assertTrue(TokenStream.isJSIdentifier("_valid"));
    }

    @Test
    public void testSpecialCases() {
        // Test edge case of empty string for isKeyword
        assertFalse(TokenStream.isKeyword(""));
        
        // Test edge case of empty string for isJSIdentifier
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}