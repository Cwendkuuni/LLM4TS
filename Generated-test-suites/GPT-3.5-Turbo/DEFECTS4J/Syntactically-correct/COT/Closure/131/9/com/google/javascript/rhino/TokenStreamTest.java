
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test with a keyword
        assertTrue(TokenStream.isKeyword("if"));

        // Test with a non-keyword
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test with a valid JS identifier
        assertTrue(TokenStream.isJSIdentifier("test123"));

        // Test with an invalid JS identifier
        assertFalse(TokenStream.isJSIdentifier("123test"));
    }

    @Test
    public void testIsKeywordNull() {
        // Test with a null input parameter
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsKeywordEmptyString() {
        // Test with an empty string input parameter
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeywordLowerCase() {
        // Test with a lowercase version of a keyword
        assertTrue(TokenStream.isKeyword("if"));
    }

    @Test
    public void testIsKeywordUpperCase() {
        // Test with an uppercase version of a keyword
        assertTrue(TokenStream.isKeyword("IF"));
    }

    @Test
    public void testIsKeywordNonKeyword() {
        // Test with a non-keyword
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifierNull() {
        // Test with a null input parameter
        assertFalse(TokenStream.isJSIdentifier(null));
    }

    @Test
    public void testIsJSIdentifierEmptyString() {
        // Test with an empty string input parameter
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifierSingleCharacter() {
        // Test with a single character input parameter
        assertTrue(TokenStream.isJSIdentifier("a"));
    }

    @Test
    public void testIsJSIdentifierCombination() {
        // Test with a combination of letters, digits, and special characters
        assertFalse(TokenStream.isJSIdentifier("t$est1"));
    }
}