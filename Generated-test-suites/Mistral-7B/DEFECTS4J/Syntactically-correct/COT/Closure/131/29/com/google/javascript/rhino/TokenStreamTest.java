import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Basic test cases for true
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("yield"));
        assertTrue(TokenStream.isKeyword("await"));

        // Basic test cases for false
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword(" "));
        assertFalse(TokenStream.isKeyword("1"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("def"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("ABCDEF"));
        assertFalse(TokenStream.isKeyword("_"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("12345"));
        assertFalse(TokenStream.isKeyword("123_45"));
        assertFalse(TokenStream.isKeyword("123_45_"));
        assertFalse(TokenStream.isKeyword("123_45_6"));

        // Testing edge cases and exceptions
        // Test case for empty string
        assertFalse(TokenStream.isKeyword(""));
        // Test case for null
        assertFalse(TokenStream.isKeyword(null));
        // Test case for null as first character
        assertFalse(TokenStream.isKeyword("null"));
        // Test case for null as last character
        assertFalse(TokenStream.isKeyword("truenull"));
        // Test case for null in middle
        assertFalse(TokenStream.isKeyword("truennull"));
        // Test case for long keywords
        assertTrue(TokenStream.isKeyword("switchcase"));
        assertTrue(TokenStream.isKeyword("switchdefault"));
        assertTrue(TokenStream.isKeyword("switchbreak"));
        assertTrue(TokenStream.isKeyword("switchcontinue"));
        assertTrue(TokenStream.isKeyword("switchreturn"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Basic test cases for true
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier(" _"));
        assertTrue(TokenStream.isJSIdentifier(" _abc"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c1"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c12"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c123"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c1234"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c12345"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c123456"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c1234567"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c12345678"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c123456789"));
        assertTrue(TokenStream.isJSIdentifier(" _ab_c1234567890"));
        // Basic test cases for false
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("abc"));
        assertFalse(TokenStream.isJSIdentifier("def"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("ABCDEF"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("12345"));
        assertFalse(TokenStream.isJSIdentifier("123_45"));
        assertFalse(TokenStream.isJSIdentifier("123_45_"));
        assertFalse(TokenStream.isJSIdentifier("123_45_6"));
        // Testing edge cases and exceptions
        // Test case for empty string
        assertTrue(TokenStream.isJSIdentifier(""));
        // Test case for null
        assertFalse(TokenStream.isJSIdentifier(null));
        // Test case for null as first character
        assertFalse(TokenStream.isJSIdentifier("null"));
        // Test case for null as last character
        assertFalse(TokenStream.isJSIdentifier("truenull"));
        // Test case for null in middle
        assertFalse(TokenStream.isJSIdentifier("truennull"));
        // Test case for long identifiers
        assertTrue(TokenStream.isJSIdentifier("longLongIdentifierName"));
        assertTrue(TokenStream.isJSIdentifier("longLongIdentifierName123"));
        assertTrue(TokenStream.isJSIdentifier("longLongIdentifierName123456"));
        assertTrue(TokenStream.isJSIdentifier("longLongIdentifierName1234567890"));
    }
}