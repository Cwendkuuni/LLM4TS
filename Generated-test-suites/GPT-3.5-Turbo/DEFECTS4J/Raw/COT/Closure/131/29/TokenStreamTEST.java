
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertFalse(TokenStream.isKeyword("foobar"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertTrue(TokenStream.isJSIdentifier("anotherIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("not a valid identifier"));
    }

    @Test
    public void testIsKeywordEdgeCases() {
        assertFalse(TokenStream.isKeyword(""));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("Int"));
        assertFalse(TokenStream.isKeyword("if("));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("$invalidIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123invalid"));
    }
}
