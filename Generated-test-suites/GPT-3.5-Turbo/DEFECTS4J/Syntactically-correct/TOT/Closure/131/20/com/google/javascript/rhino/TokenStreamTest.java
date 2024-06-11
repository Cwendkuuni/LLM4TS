import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Typical Use Cases
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("catch"));

        // Edge Cases
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("class"));

        // Error Scenarios
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("random"));
        assertFalse(TokenStream.isKeyword("12345"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Typical Use Cases
        assertTrue(TokenStream.isJSIdentifier("firstName"));
        assertTrue(TokenStream.isJSIdentifier("lastName"));
        assertTrue(TokenStream.isJSIdentifier("age"));

        // Edge Cases
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("x1"));

        // Error Scenarios
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("first name"));
    }
}