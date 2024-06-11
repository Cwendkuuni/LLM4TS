import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("hello"));
        assertFalse(TokenStream.isKeyword("world"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("hello"));
        assertTrue(TokenStream.isJSIdentifier("world"));
        assertFalse(TokenStream.isJSIdentifier("if"));
        assertFalse(TokenStream.isJSIdentifier("for"));
    }
}