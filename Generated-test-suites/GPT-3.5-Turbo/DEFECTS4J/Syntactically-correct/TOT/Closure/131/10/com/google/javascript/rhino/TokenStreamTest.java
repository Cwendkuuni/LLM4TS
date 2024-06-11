import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword_ValidKeyword_ReturnsTrue() {
        String keyword = "if";
        boolean result = TokenStream.isKeyword(keyword);
        assertTrue(result);
    }
    
    @Test
    public void testIsKeyword_InvalidKeyword_ReturnsFalse() {
        String keyword = "notkeyword";
        boolean result = TokenStream.isKeyword(keyword);
        assertFalse(result);
    }
    
    @Test
    public void testIsKeyword_KeywordWithUpperCaseLetters_ReturnsTrue() {
        String keyword = "ELSE";
        boolean result = TokenStream.isKeyword(keyword);
        assertTrue(result);
    }
    
    @Test
    public void testIsJSIdentifier_ValidIdentifier_ReturnsTrue() {
        String identifier = "validIdentifier";
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertTrue(result);
    }
    
    @Test
    public void testIsJSIdentifier_InvalidIdentifier_ReturnsFalse() {
        String identifier = "invalid-identifier";
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertFalse(result);
    }
    
    @Test
    public void testIsJSIdentifier_EmptyString_ReturnsFalse() {
        String identifier = "";
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertFalse(result);
    }
    
    @Test
    public void testIsJSIdentifier_IdentifierStartingWithNumber_ReturnsFalse() {
        String identifier = "1abc";
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertFalse(result);
    }
    
    @Test
    public void testIsJSIdentifier_IdentifierContainingSpecialCharacters_ReturnsFalse() {
        String identifier = "abc@def";
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertFalse(result);
    }
}