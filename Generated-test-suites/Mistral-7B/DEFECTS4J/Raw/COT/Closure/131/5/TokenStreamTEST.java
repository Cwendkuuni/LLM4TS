import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_SimpleCases() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
    }

    @Test
    public void testIsKeyword_TwoLetterKeywords() {
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("else if"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("yield"));
    }

    @Test
    public void testIsKeyword_ThreeLetterKeywords() {
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("as"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("native"));
    }

    @Test
    public void testIsKeyword_FourLetterKeywords() {
        assertTrue(TokenStream.isKeyword("breakpoint")); // not a keyword but included for completeness
        assertTrue(TokenStream.isKeyword("byte"));
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
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyboard("function"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("throws"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("with"));
        assertTrue(TokenStream.isKeyword("yield"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_NullInput() {
        TokenStream.isKeyword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_EmptyInput() {
        TokenStream.isKeyword("");
    }

    @Test
    public void testIsJSIdentifier_SimpleCases() {
        assertTrue(TokenStream.isJSIdentifier("identifier1"));
        assertTrue(TokenStream.isJSIdentifier("identifier_1"));
        assertTrue(TokenStream.isJSIdentifier("identifier_123"));
        assertTrue(TokenStream.isJSIdentifier("identifier123"));
    }

    @Test
    public void testIsJSIdentifier_SpecialChars() {
        assertFalse(TokenStream.isJSIdentifier("identifier1%"));
        assertFalse(TokenStream.isJSIdentifier("identifier1$"));
        assertFalse(TokenStream.isJSIdentifier("identifier1#"));
        assertFalse(TokenStream.isJSIdentifier("identifier1^"));
        assertFalse(TokenStream.isJSIdentifier("identifier1&"));
        assertFalse(TokenStream.isJSIdentifier("identifier1|"));
    }

    @Test
    public void testIsJSIdentifier_InvalidChars() {
        assertFalse(TokenStream.isJSIdentifier("identifier1!"));
        assertFalse(TokenStream.isJSIdentifier("identifier1?"));
        assertFalse(TokenStream.isJSIdentifier("identifier1@"));
        assertFalse(TokenStream.isJSIdentifier("identifier1("));
        assertFalse(TokenStream.isJSIdentifier("identifier1)"));
        assertFalse(TokenStream.isJSIdentifier("identifier1["));
        assertFalse(TokenStream.isJSIdentifier("identifier1]"));
        assertFalse(TokenStream.isJSIdentifier("identifier1{"));
        assertFalse(TokenStream.isJSIdentifier("identifier1}"));
        assertFalse(TokenStream.isJSIdentifier("identifier1;"));
        assertFalse(TokenStream.isJSIdentifier("identifier1:"));
        assertFalse(TokenStream.isJSIdentifier("identifier1,"));
        assertFalse(TokenStream.isJSIdentifier("identifier1."));
        assertFalse(TokenStream.isJSIdentifier("identifier1="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_NullInput() {
        TokenStream.isJSIdentifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_EmptyInput() {
        TokenStream.isJSIdentifier("");
    }
}