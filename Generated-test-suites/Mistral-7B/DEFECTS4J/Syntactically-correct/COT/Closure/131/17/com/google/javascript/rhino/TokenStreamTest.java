import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_ValidKeywords() {
        String[] keywords = {"if", "else", "while", "for", "switch", "case", "default", "break", "continue", "return", "false", "true", "null", "void", "class", "const", "final", "float", "short", "super", "throw", "catch", "native", "delete", "return", "throws", "import", "double", "static", "public", "switch", "export", "package", "abstract", "continue", "debugger", "function", "interface", "protected", "transient", "implements", "instanceof", "synchronized"};

        for (String keyword : keywords) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_InvalidString() {
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("abcd"));
        assertFalse(TokenStream.isKeyword("!@#$%^&*"));
    }

    @Test
    public void testIsJSIdentifier_ValidJavaIdentifier() {
        String[] identifiers = {"identifier1", "_identifier2", "identifier_3", "identifierWithNumber_1", "identifierWithNumber_2_3", "identifierWithUnderScore", "identifierWithCapitalLetter_1"};

        for (String identifier : identifiers) {
            assertTrue(TokenStream.isJSIdentifier(identifier));
        }
    }

    @Test
    public void testIsJSIdentifier_InvalidJavaIdentifier() {
        String[] invalidIdentifiers = {"123", "abcd", "!@#$%^&*", "identifier-with-hyphen", "identifier with space"};

        for (String invalidIdentifier : invalidIdentifiers) {
            assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
        }
    }
}