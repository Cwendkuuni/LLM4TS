import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_Basic() {
        String[] keywords = {"if", "else", "while", "for", "return", "void", "int", "long", "short", "float", "double", "boolean", "true", "false", "null", "package", "import", "export", "class", "interface", "extends", "implements", "instanceof", "new", "this", "super", "static", "final", "const", "private", "protected", "public", "abstract", "transient", "synchronized", "throw", "catch", "try", "finally", "break", "continue", "case", "default"};

        for (String keyword : keywords) {
            boolean result = TokenStream.isKeyword(keyword);
            assertTrue("Expected true for keyword: " + keyword, result);
        }
    }

    @Test
    public void testIsKeyword_InvalidIdentifier() {
        boolean result = TokenStream.isKeyword("invalidIdentifier");
        assertFalse("Expected false for invalid identifier", result);
    }

    @Test
    public void testIsJSIdentifier_Basic() {
        String[] identifiers = {"identifier1", "identifier2", "identifier3", "identifier4", "identifier5", "identifier6", "identifier7", "identifier8", "identifier9", "identifier10", "identifier11", "identifier12"};

        for (String identifier : identifiers) {
            boolean result = TokenStream.isJSIdentifier(identifier);
            assertTrue("Expected true for valid identifier: " + identifier, result);
        }
    }

    @Test
    public void testIsJSIdentifier_InvalidIdentifier() {
        String[] invalidIdentifiers = {"invalid_identifier1", "invalid_identifier2", "invalid_identifier3", "invalid_identifier4", "invalid_identifier5", "invalid_identifier6", "invalid_identifier7", "invalid_identifier8", "invalid_identifier9", "invalid_identifier10", "invalid_identifier11", "invalid_identifier12"};

        for (String invalidIdentifier : invalidIdentifiers) {
            boolean result = TokenStream.isJSIdentifier(invalidIdentifier);
            assertFalse("Expected false for invalid identifier: " + invalidIdentifier, result);
        }
    }
}