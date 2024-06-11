import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_ValidKeyword() {
        String[] keywords = {"if", "else", "switch", "for", "while", "do", "break", "return", "this", "typeof", "new", "var", "let", "const", "import", "export", "package", "class", "interface", "function", "abstract", "public", "private", "protected", "static", "const", "volatile", "transient", "synchronized", "default", "try", "catch", "finally", "throw"};
        for (String keyword : keywords) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_InvalidIdentifier() {
        String[] invalidIdentifiers = {"invalidKeyword123", "invalid_identifier", "invalidIdentifierWithDigit", "invalidIdentifierWithUnderScore"};
        for (String invalidIdentifier : invalidIdentifiers) {
            assertFalse(TokenStream.isKeyword(invalidIdentifier));
        }
    }

    @Test
    public void testIsJSIdentifier_ValidIdentifier() {
        String[] validIdentifiers = {"validIdentifier1", "validIdentifier2", "validIdentifierWithDigit", "validIdentifierWithUnderScore", "validIdentifierWithDollarSign"};
        for (String validIdentifier : validIdentifiers) {
            assertTrue(TokenStream.isJSIdentifier(validIdentifier));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testIsJSIdentifier_NullIdentifier() {
        String nullIdentifier = null;
        assertFalse(TokenStream.isJSIdentifier(nullIdentifier));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_EmptyIdentifier() {
        String emptyIdentifier = "";
        assertFalse(TokenStream.isJSIdentifier(emptyIdentifier));
    }

    @Test
    public void testIsJSIdentifier_IdentifierStartCharacter() {
        String identifierStartCharacter = "_";
        assertTrue(TokenStream.isJSIdentifier(identifierStartCharacter));
    }

    @Test
    public void testIsJSIdentifier_IdentifierPartCharacter() {
        String identifierPartCharacter = "a";
        assertTrue(TokenStream.isJSIdentifier(identifierPartCharacter));
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithDigit() {
        String identifierWithDigit = "identifier123";
        assertTrue(TokenStream.isJSIdentifier(identifierWithDigit));
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithUnderScore() {
        String identifierWithUnderScore = "identifier_with_under_score";
        assertTrue(TokenStream.isJSIdentifier(identifierWithUnderScore));
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithDollarSign() {
        String identifierWithDollarSign = "identifier$withDollarSign";
        assertTrue(TokenStream.isJSIdentifier(identifierWithDollarSign));
    }
}