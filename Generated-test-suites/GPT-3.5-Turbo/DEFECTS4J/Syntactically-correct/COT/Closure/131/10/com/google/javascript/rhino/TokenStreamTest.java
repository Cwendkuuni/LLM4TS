import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_NullInput_ReturnsFalse() {
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsKeyword_EmptyStringInput_ReturnsFalse() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeyword_NotAKeywordInput_ReturnsFalse() {
        assertFalse(TokenStream.isKeyword("random"));
    }

    @Test
    public void testIsKeyword_LowercaseInput_ReturnsTrue() {
        assertTrue(TokenStream.isKeyword("case"));
    }

    @Test
    public void testIsKeyword_UppercaseInput_ReturnsTrue() {
        assertTrue(TokenStream.isKeyword("BREAK"));
    }

    @Test
    public void testIsJSIdentifier_NullInput_ReturnsFalse() {
        assertFalse(TokenStream.isJSIdentifier(null));
    }

    @Test
    public void testIsJSIdentifier_EmptyStringInput_ReturnsFalse() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifier_StartingWithDigit_ReturnsFalse() {
        assertFalse(TokenStream.isJSIdentifier("123abc"));
    }

    @Test
    public void testIsJSIdentifier_ContainsSpecialCharacters_ReturnsFalse() {
        assertFalse(TokenStream.isJSIdentifier("@hello"));
    }

    @Test
    public void testIsJSIdentifier_StartingWithUnderscore_ReturnsTrue() {
        assertTrue(TokenStream.isJSIdentifier("_variable"));
    }

}

