import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertFalse(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertFalse(TokenStream.isKeyword("incorrect"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("testing"));
        assertFalse(TokenStream.isJSIdentifier("123test"));
        assertTrue(TokenStream.isJSIdentifier("valid_identifier"));
        assertFalse(TokenStream.isJSIdentifier("invalid identifier"));
        assertTrue(TokenStream.isJSIdentifier("identifier123"));
    }
}