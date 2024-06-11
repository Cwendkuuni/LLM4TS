import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("while"));
        assertFalse(TokenStream.isKeyword("hello"));
        assertFalse(TokenStream.isKeyword("123abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("hello"));
        assertTrue(TokenStream.isJSIdentifier("_var"));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("@var"));
    }
}