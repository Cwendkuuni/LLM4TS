import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("invalid identifier"));
    }

    @Test
    public void testIsJSIdentifierEmptyString() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifierSpecialCharacters() {
        assertFalse(TokenStream.isJSIdentifier("$invalid#"));
    }

    @Test
    public void testIsJSIdentifierLongInput() {
        assertFalse(TokenStream.isJSIdentifier("abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }
}