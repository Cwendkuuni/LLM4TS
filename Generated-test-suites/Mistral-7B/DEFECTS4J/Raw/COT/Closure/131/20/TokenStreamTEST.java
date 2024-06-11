import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_EmptyString() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeyword_SingleCharacter() {
        for (char c = 'a'; c <= 'z'; c++) {
            assertFalse(TokenStream.isKeyword(Character.toString(c)));
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            assertFalse(TokenStream.isKeyword(Character.toString(c)));
        }

        assertFalse(TokenStream.isKeyword("_"));
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("0"));
        assertFalse(TokenStream.isKeyword("1"));
        assertFalse(TokenStream.isKeyword("2"));
        assertFalse(TokenStream.isKeyword("3"));
        assertFalse(TokenStream.isKeyword("4"));
        assertFalse(TokenStream.isKeyword("5"));
        assertFalse(TokenStream.isKeyword("6"));
        assertFalse(TokenStream.isKeyword("7"));
        assertFalse(TokenStream.isKeyword("8"));
        assertFalse(TokenStream.isKeyword("9"));
    }

    @Test
    public void testIsKeyword_TwoLetterKeywords() {
        for (String keyword : new String[]{"if", "else", "for", "while", "do", "switch", "case", "try", "catch", "throw", "new", "var", "let", "const", "return", "this", "super", "class", "export", "import", "package", "public", "private", "protected", "static", "const", "final", "break", "continue", "return", "throws", "default", "finally", "delete", "void", "null", "false", "true", "typeof", "let", "yield"}) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_ThreeLetterKeywords() {
        for (String keyword : new String[]{"abstract", "boolean", "byte", "catch", "class", "const", "debugger", "default", "delete", "do", "else", "export", "extends", "false", "finally", "float", "for", "function", "goto", "if", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "return", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"}) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_FourLetterKeywords() {
        for (String keyword : new String[]{"breakpoint", "caseclass", "catchtry", "classcast", "constassert", "defaultcase", "dowhile", "doubleword", "elseif", "extendssuper", "finallyfinally", "float32", "gotoif", "ifelse", "implementsinterface", "importstatic", "int8", "int16", "int32", "longlong", "nativeable", "newinstance", "packagename", "privateprotected", "protectedstatic", "returnvoid", "superclass", "synchronizedstatic", "thiskeyword", "throwthrows", "transientfinal", "tryfinally", "trywithresources", "typeofname", "varargs", "volatilenull", "whiledo"}) {
            assertFalse(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsJSIdentifier_InvalidIdentifier() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("abc123"));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("ABC-123"));
        assertFalse(TokenStream.isJSIdentifier("ab$c"));
        assertFalse(TokenStream.isJSIdentifier("my-identifier"));
        assertFalse(TokenStream.isJSIdentifier("myIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("my Identifier"));
        assertFalse(TokenStream.isJSIdentifier("my Identifier "));
        assertFalse(TokenStream.isJSIdentifier("my Identifier_"));
        assertFalse(TokenStream.isJSIdentifier("my Identifier_ "));
    }

    @Test
    public void testIsJSIdentifier_ValidIdentifier() {
        for (String identifier : new String[]{"identifier1", "identifier_1", "identifier1_", "identifier_1_"}) {
            assertTrue(TokenStream.isJSIdentifier(identifier));
        }
    }
}