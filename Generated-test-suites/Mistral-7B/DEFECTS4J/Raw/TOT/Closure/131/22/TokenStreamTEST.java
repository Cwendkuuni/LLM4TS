import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Testing valid keywords
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("char"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("String"));
        assertTrue(TokenStream.isKeyword("Class"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("throws"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));

        // Testing invalid keywords
        assertFalse(TokenStream.isKeyword("invalidKeyword"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("MyClass"));
        assertFalse(TokenStream.isKeyword("my_method"));
        assertFalse(TokenStream.isKeyword("My_Method"));
        assertFalse(TokenStream.isKeyword("myMethod"));
        assertFalse(TokenStream.isKeyword("myMethod123"));
        assertFalse(TokenStream.isKeyword("my_Method123"));
        assertFalse(TokenStream.isKeyword("my_Method_123"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Testing valid JS identifiers
        assertTrue(TokenStream.isJSIdentifier("validIdentifier1"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier2"));
        assertTrue(TokenStream.isJSIdentifier("_validIdentifier3"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier_1"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier123"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier_123"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier_123_"));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier_123__"));

        // Testing invalid JS identifiers
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("identifier123"));
        assertFalse(TokenStream.isJSIdentifier("identifier-123"));
        assertFalse(TokenStream.isJSIdentifier("identifier123-"));
        assertFalse(TokenStream.isJSIdentifier("identifier123__"));
        assertFalse(TokenStream.isJSIdentifier("identifier123__"));
    }
}