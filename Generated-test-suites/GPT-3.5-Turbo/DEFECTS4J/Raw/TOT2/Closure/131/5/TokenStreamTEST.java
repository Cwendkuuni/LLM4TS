// Complete JUnit test file for TokenStream class
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("xyz"));
        assertTrue(TokenStream.isKeyword("void"));
        assertFalse(TokenStream.isKeyword("abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("invalid-identifier"));
        assertTrue(TokenStream.isJSIdentifier("newVariable"));
        assertFalse(TokenStream.isJSIdentifier("9invalid"));
    }

    @Test
    public void testIsKeywordEdgeCases() {
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("abc"));
        assertTrue(TokenStream.isKeyword("class"));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("invalid-identifier"));
    }
}