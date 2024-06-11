import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_SingleCharacter() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        // Add other single character keywords here
    }

    @Test
    public void testIsKeyword_TwoCharacter() {
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        // Add other two character keywords here
    }

    @Test
    public void testIsKeyword_ThreeCharacter() {
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("true"));
        // Add other three character keywords here
    }

    @Test
    public void testIsKeyword_FourCharacter() {
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("byte"));
        // Add other four character keywords here
    }

    @Test
    public void testIsKeyword_FiveCharacter() {
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("class"));
        // Add other five character keywords here
    }

    @Test
    public void testIsKeyword_SixCharacter() {
        assertTrue(TokenStream.isKeyword("native"));
        // Add other six character keywords here
    }

    @Test
    public void testIsKeyword_SevenCharacter() {
        assertTrue(TokenStream.isKeyword("package"));
        // Add other seven character keywords here
    }

    @Test
    public void testIsKeyword_EightCharacter() {
        assertTrue(TokenStream.isKeyword("abstract"));
        // Add other eight character keywords here
    }

    @Test
    public void testIsKeyword_NineCharacter() {
        assertTrue(TokenStream.isKeyword("interface"));
        // Add other nine character keywords here
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("camelCase"));
        assertTrue(TokenStream.isJSIdentifier("underscore_case"));
        assertTrue(TokenStream.isJSIdentifier("validate_email"));
        assertTrue(TokenStream.isJSIdentifier("my_function"));
        // Add other valid JavaScript identifiers here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_EmptyString() {
        TokenStream.isJSIdentifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_InvalidCharacter() {
        String invalidIdentifier = "Identifier_with_invalid_character!";
        TokenStream.isJSIdentifier(invalidIdentifier);
    }

}