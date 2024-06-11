import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("foo"));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("_test"));
        assertFalse(TokenStream.isJSIdentifier("test-123"));
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}