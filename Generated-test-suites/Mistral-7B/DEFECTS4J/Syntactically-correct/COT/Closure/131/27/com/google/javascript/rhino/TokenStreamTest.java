import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Basic functionality test
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("else if"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("void"));
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
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("with"));

        // Edge cases and exception handling tests
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword(" "));
        assertFalse(TokenStream.isKeyword("1"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("ABC"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("123ABC"));

        // Test for invalid keyword length
        assertFalse(TokenStream.isKeyword("keyword123456"));

        // Test for invalid keyword characters
        assertFalse(TokenStream.isKeyword("if#"));
        assertFalse(TokenStream.isKeyword("if%"));
        assertFalse(TokenStream.isKeyword("if!"));
        assertFalse(TokenStream.isKeyword("if?"));
        assertFalse(TokenStream.isKeyword("if@"));
        assertFalse(TokenStream.isKeyword("if$"));
        assertFalse(TokenStream.isKeyword("if&"));
        assertFalse(TokenStream.isKeyword("if*"));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if)"));
        assertFalse(TokenStream.isKeyword("if{"));
        assertFalse(TokenStream.isKeyword("if}"));
        assertFalse(TokenStream.isKeyword("if["));
        assertFalse(TokenStream.isKeyword("if]"));
        assertFalse(TokenStream.isKeyword("if<"));
        assertFalse(TokenStream.isKeyword("if>"));
        assertFalse(TokenStream.isKeyword("if;"));

        // Test for null input
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsJSIdentifier() {
        // Basic functionality test
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("ABC"));
        assertTrue(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("_123"));
        assertTrue(TokenStream.isJSIdentifier("abc123"));
        assertTrue(TokenStream.isJSIdentifier("ABC123"));
        assertTrue(TokenStream.isJSIdentifier("abc_123"));
        assertTrue(TokenStream.isJSIdentifier("ABC_123"));
        assertTrue(TokenStream.isJSIdentifier("_abc123"));
        assertTrue(TokenStream.isJSIdentifier("_ABC123"));

        // Edge cases and exception handling tests
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("123ABC"));
        assertFalse(TokenStream.isJSIdentifier("123ABC_"));
        assertFalse(TokenStream.isJSIdentifier("_123ABC"));
        assertFalse(TokenStream.isJSIdentifier("_123ABC_"));
        assertFalse(TokenStream.isJSIdentifier("1_23"));
        assertFalse(TokenStream.isJSIdentifier("1_23ABC"));
        assertFalse(TokenStream.isJSIdentifier("1_23ABC_"));
        assertFalse(TokenStream.isJSIdentifier("_1_23ABC"));
        assertFalse(TokenStream.isJSIdentifier("_1_23ABC_"));
        assertFalse(TokenStream.isJSIdentifier("abc "));
        assertFalse(TokenStream.isJSIdentifier("abc123 "));
        assertFalse(TokenStream.isJSIdentifier("abc123 "));
        assertFalse(TokenStream.isJSIdentifier("123 abc"));
        assertFalse(TokenStream.isJSIdentifier("123 abc "));
        assertFalse(TokenStream.isJSIdentifier("123 abc_"));
        assertFalse(TokenStream.isJSIdentifier("123 _abc"));
        assertFalse(TokenStream.isJSIdentifier("123 _abc_"));

        // Test for invalid identifier characters
        assertFalse(TokenStream.isJSIdentifier("abc#"));
        assertFalse(TokenStream.isJSIdentifier("abc%"));
        assertFalse(TokenStream.isJSIdentifier("abc!"));
        assertFalse(TokenStream.isJSIdentifier("abc?"));
        assertFalse(TokenStream.isJSIdentifier("abc@"));
        assertFalse(TokenStream.isJSIdentifier("abc$"));
        assertFalse(TokenStream.isJSIdentifier("abc&"));
        assertFalse(TokenStream.isJSIdentifier("abc*"));
        assertFalse(TokenStream.isJSIdentifier("abc("));
        assertFalse(TokenStream.isJSIdentifier("abc)"));
        assertFalse(TokenStream.isJSIdentifier("abc{"));
        assertFalse(TokenStream.isJSIdentifier("abc}"));
        assertFalse(TokenStream.isJSIdentifier("abc["));
        assertFalse(TokenStream.isJSIdentifier("abc]"));
        assertFalse(TokenStream.isJSIdentifier("abc<"));
        assertFalse(TokenStream.isJSIdentifier("abc>"));
        assertFalse(TokenStream.isJSIdentifier("abc;"));

        // Test for null input
        assertFalse(TokenStream.isJSIdentifier(null));
    }
}