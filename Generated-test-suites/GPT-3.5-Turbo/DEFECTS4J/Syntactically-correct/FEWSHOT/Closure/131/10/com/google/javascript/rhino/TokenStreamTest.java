import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("foo"));
        assertFalse(TokenStream.isKeyword("bar"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword(""));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("_bar"));
        assertTrue(TokenStream.isJSIdentifier("isJavaScript"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("foo bar"));
    }
}