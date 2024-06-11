import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("void"));
        assertFalse(TokenStream.isKeyword("random"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123Invalid"));
    }

    @Test
    public void testIsKeywordEdgeCases() {
        assertFalse(TokenStream.isKeyword(""));
        assertTrue(TokenStream.isKeyword("abstract"));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertFalse(TokenStream.isJSIdentifier("_invalid"));
        assertTrue(TokenStream.isJSIdentifier("valid123Identifier"));
    }

    @Test
    public void testIsKeywordExceptions() {
        try {
            TokenStream.isKeyword(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @Test
    public void testIsJSIdentifierExceptions() {
        try {
            TokenStream.isJSIdentifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }
}