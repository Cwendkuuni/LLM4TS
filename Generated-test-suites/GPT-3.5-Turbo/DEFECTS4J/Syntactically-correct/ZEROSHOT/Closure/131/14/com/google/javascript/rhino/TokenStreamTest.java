import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("while"));

        assertFalse(TokenStream.isKeyword("test"));
        assertFalse(TokenStream.isKeyword("1234"));
        assertFalse(TokenStream.isKeyword("x1ab"));
        assertFalse(TokenStream.isKeyword("true"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("x1ab"));
        assertTrue(TokenStream.isJSIdentifier("myVariable"));
        assertTrue(TokenStream.isJSIdentifier("_private"));

        assertFalse(TokenStream.isJSIdentifier("1234"));
        assertFalse(TokenStream.isJSIdentifier("#$%^&*"));
        assertFalse(TokenStream.isJSIdentifier("x!"));
        assertFalse(TokenStream.isJSIdentifier("123variable"));
    }
}