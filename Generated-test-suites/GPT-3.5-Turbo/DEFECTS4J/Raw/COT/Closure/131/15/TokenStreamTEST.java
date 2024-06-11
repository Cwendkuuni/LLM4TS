import com.google.javascript.rhino.TokenStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test Case 1: Valid keyword - "if"
        assertTrue(TokenStream.isKeyword("if"));

        // Test Case 2: Valid keyword - "else"
        assertTrue(TokenStream.isKeyword("else"));

        // Test Case 3: Invalid keyword - "for"
        assertFalse(TokenStream.isKeyword("for"));

        // Test Case 4: Invalid keyword - "while"
        assertFalse(TokenStream.isKeyword("while"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test Case 1: Valid identifier - "name"
        assertTrue(TokenStream.isJSIdentifier("name"));

        // Test Case 2: Valid identifier - "_value"
        assertTrue(TokenStream.isJSIdentifier("_value"));

        // Test Case 3: Invalid identifier - "1test"
        assertFalse(TokenStream.isJSIdentifier("1test"));

        // Test Case 4: Invalid identifier - "let"
        assertFalse(TokenStream.isJSIdentifier("let"));
    }

}