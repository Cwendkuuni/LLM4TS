import static com.google.javascript.rhino.TokenStream.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        // Test valid keywords
        assertTrue(isKeyword("if"));
        assertTrue(isKeyword("else"));
        assertTrue(isKeyword("while"));
        assertTrue(isKeyword("for"));
        assertTrue(isKeyword("return"));
        assertTrue(isKeyword("true"));
        assertTrue(isKeyword("false"));
        assertTrue(isKeyword("null"));
        assertTrue(isKeyword("class"));
        assertTrue(isKeyword("export"));
        assertTrue(isKeyword("import"));

        // Test invalid keywords
        assertFalse(isKeyword(""));
        assertFalse(isKeyword("myVariable"));
        assertFalse(isKeyword("123"));
        assertFalse(isKeyword("myFunction"));
        assertFalse(isKeyword("myClass"));

        // Edge cases
        assertTrue(isKeyword("int"));
        assertTrue(isKeyword("void"));
        assertTrue(isKeyword("break"));
        assertTrue(isKeyword("continue"));
        assertTrue(isKeyword("default"));
        assertTrue(isKeyword("false"));
        assertTrue(isKeyword("null"));
        assertTrue(isKeyword("true"));
        assertTrue(isKeyword("package"));
        assertTrue(isKeyword("import"));
        assertTrue(isKeyword("export"));
        assertTrue(isKeyword("interface"));
        assertTrue(isKeyword("abstract"));
        assertTrue(isKeyword("protected"));
        assertTrue(isKeyword("public"));
        assertTrue(isKeyword("implements"));
        assertTrue(isKeyword("extends"));
        assertTrue(isKeyword("static"));
        assertTrue(isKeyword("synchronized"));
        assertTrue(isKeyword("case"));
        assertTrue(isKeyword("default"));
        assertTrue(isKeyword("throw"));
        assertTrue(isKeyword("try"));
        assertTrue(isKeyword("catch"));
        assertTrue(isKeyword("finally"));

        // Error cases
        assertFalse(isKeyword(""));
        assertFalse(isKeyword("123"));
        assertFalse(isKeyword("myFunction("));
        assertFalse(isKeyword("myClass("));
        assertFalse(isKeyword("myVariable123"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test valid identifiers
        assertTrue(isJSIdentifier("myVariable"));
        assertTrue(isJSIdentifier("myFunction"));
        assertTrue(isJSIdentifier("myClass"));
        assertTrue(isJSIdentifier("_myVariable"));
        assertTrue(isJSIdentifier("my_variable"));
        assertTrue(isJSIdentifier("myVariable123"));

        // Test invalid identifiers
        assertFalse(isJSIdentifier(""));
        assertFalse(isJSIdentifier("123"));
        assertFalse(isJSIdentifier("myFunction("));
        assertFalse(isJSIdentifier("myClass("));
        assertFalse(isJSIdentifier("myVariable123="));

        // Edge cases
        assertTrue(isJSIdentifier("int"));
        assertTrue(isJSIdentifier("void"));
        assertTrue(isJSIdentifier("myVariable123"));
        assertTrue(isJSIdentifier("myFunctionName"));
        assertTrue(isJSIdentifier("myClass name"));

        // Error cases
        assertFalse(isJSIdentifier(""));
        assertFalse(isJSIdentifier("123"));
        assertFalse(isJSIdentifier("myFunction("));
        assertFalse(isJSIdentifier("myClass("));
        assertFalse(isJSIdentifier("myVariable123="));
    }
}