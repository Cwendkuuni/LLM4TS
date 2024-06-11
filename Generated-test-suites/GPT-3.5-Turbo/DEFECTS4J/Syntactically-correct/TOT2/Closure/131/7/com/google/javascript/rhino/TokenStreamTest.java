import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("do"));
        assertFalse(TokenStream.isKeyword("abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertTrue(TokenStream.isJSIdentifier("anotherValid123"));
        assertFalse(TokenStream.isJSIdentifier("not_valid"));
    }

    @Test
    public void testIsKeywordEdgeCases() {
        assertFalse(TokenStream.isKeyword("notakeyword"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("delete"));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertTrue(TokenStream.isJSIdentifier("$valid"));
        assertFalse(TokenStream.isJSIdentifier("123no"));
    }

    @Test
    public void testIsKeywordExceptions() {
        assertFalse(TokenStream.isKeyword(null));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifierExceptions() {
        assertFalse(TokenStream.isJSIdentifier(null));
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}