import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_ValidKeywords() {
        String[] keywords = {"if", "else", "else if", "return", "while", "for", "new", "class", "void", "boolean", "true", "false", "null", "package", "import", "export", "this", "super", "break", "continue", "delete", "throw", "try", "catch", "finally", "new", "instanceof", "in", "var", "let", "const", "interface", "implements", "extends", "function", "abstract", "final", "static", "public", "protected", "transient", "synchronized", "yield", "importas", "as", "export default"};

        for (String keyword : keywords) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_InvalidString() {
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("abcd"));
        assertFalse(TokenStream.isKeyword("AbCd"));
        assertFalse(TokenStream.isKeyword("ABCd"));
    }

    @Test
    public void testIsJSIdentifier_ValidIdentifier() {
        String[] identifiers = {"identifier1", "identifier_2", "identifier3_four", "identifier5", "_identifier", "identifier_with_digits_123", "identifierWith_underscore_and_digit_123", "identifierWith_dollar_sign_$", "identifierWith_double_quotes\".identifier\""};

        for (String identifier : identifiers) {
            assertTrue(TokenStream.isJSIdentifier(identifier));
        }
    }

    @Test
    public void testIsJSIdentifier_InvalidString() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("abcd"));
        assertFalse(TokenStream.isJSIdentifier("AbCd"));
        assertFalse(TokenStream.isJSIdentifier("ABCd"));
        assertFalse(TokenStream.isJSIdentifier("identifier@with_special_char"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_leading_underscore_"));
        assertFalse(TokenStream.isJSIdentifier("identifier-with_hyphen"));
    }
}