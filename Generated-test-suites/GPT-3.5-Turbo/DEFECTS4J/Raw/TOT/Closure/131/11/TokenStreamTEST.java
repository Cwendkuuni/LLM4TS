import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Typical Use Case
        assertTrue(TokenStream.isKeyword("if"));

        // Edge Cases
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("volatile"));

        // Error Scenarios
        assertFalse(TokenStream.isKeyword("While"));
        assertFalse(TokenStream.isKeyword("falSE"));
        assertFalse(TokenStream.isKeyword("Im"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Typical Use Case
        assertTrue(TokenStream.isJSIdentifier("myVariable"));

        // Edge Cases
        assertTrue(TokenStream.isJSIdentifier("_underscore123"));
        assertTrue(TokenStream.isJSIdentifier("$money"));

        // Error Scenarios
        assertFalse(TokenStream.isJSIdentifier("123number"));
        assertFalse(TokenStream.isJSIdentifier("let'sgo"));
        assertFalse(TokenStream.isJSIdentifier("@illegal"));
    }
}