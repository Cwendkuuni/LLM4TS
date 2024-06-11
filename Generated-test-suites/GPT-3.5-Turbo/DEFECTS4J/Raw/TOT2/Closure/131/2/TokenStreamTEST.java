import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertFalse(TokenStream.isKeyword("test"));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("test123"));
        assertFalse(TokenStream.isJSIdentifier("_test"));
        assertFalse(TokenStream.isJSIdentifier("123test"));
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}