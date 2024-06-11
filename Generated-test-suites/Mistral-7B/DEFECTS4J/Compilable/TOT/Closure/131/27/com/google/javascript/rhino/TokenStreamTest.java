package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test positive cases
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
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("char"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));

        // Test negative cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("myVar"));
        assertFalse(TokenStream.isKeyword("my_var"));
        assertFalse(TokenStream.isKeyword("My_Var"));
        assertFalse(TokenStream.isKeyword("myVar123"));
        assertFalse(TokenStream.isKeyword("MyVar123"));
        assertFalse(TokenStream.isKeyword("MyVar_123"));
        assertFalse(TokenStream.isKeyword("myVar_123"));
        assertFalse(TokenStream.isKeyword("my_Var_123"));
        assertFalse(TokenStream.isKeyword("myVar123_"));
        assertFalse(TokenStream.isKeyword("MyVar123_"));
        assertFalse(TokenStream.isKeyword("MyVar_123"));
        assertFalse(TokenStream.isKeyword("myVar_123_"));
        assertFalse(TokenStream.isKeyword("myVar_123_123"));
        assertFalse(TokenStream.isKeyword("MyVar_123_123"));
        assertFalse(TokenStream.isKeyword("MyVar_123_123"));
        assertFalse(TokenStream.isKeyword("MyVar_123_123_"));
        assertFalse(TokenStream.isKeyword("MyVar_123_123__"));
        assertFalse(TokenStream.isKeyword("MyVar_123_123___"));

        // Test other cases
        assertNull(TokenStream.isKeyword(null));
        assertNull(TokenStream.isKeyword(""));
        assertNull(TokenStream.isKeyword(""));
        assertNull(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test positive cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        assertTrue(TokenStream.isJSIdentifier("my_var"));
        assertTrue(TokenStream.isJSIdentifier("My_Var"));
        assertTrue(TokenStream.isJSIdentifier("myVar123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123"));
        assertTrue(TokenStream.isJSIdentifier("myVar_123"));
        assertTrue(TokenStream.isJSIdentifier("my_Var_123"));
        assertTrue(TokenStream.isJSIdentifier("myVar_123_"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_123_"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_123__"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_123___"));

        // Test negative cases
        assertFalse(TokenStream.isJSIdentifier("myVar123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123"));
        assertFalse(TokenStream.isJSIdentifier("my_Var_123"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123_"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123_"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123__"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123___"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));
        assertFalse(TokenStream.isJSIdentifier("MyVar_123_123"));

        // Test other cases
        assertNull(TokenStream.isJSIdentifier(null));
        assertNull(TokenStream.isJSIdentifier(""));
        assertNull(TokenStream.isJSIdentifier(""));
        assertNull(TokenStream.isJSIdentifier(""));
    }
}