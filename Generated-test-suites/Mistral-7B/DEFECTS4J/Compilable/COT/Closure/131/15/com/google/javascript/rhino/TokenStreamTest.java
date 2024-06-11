package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Basic functionality test
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        // Edge cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("1"));
        assertFalse(TokenStream.isKeyword("1if"));
        assertFalse(TokenStream.isKeyword("if1"));
        assertFalse(TokenStream.isKeyword("if "));
        assertFalse(TokenStream.isKeyword(" if "));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if)"));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if("));
        assertFalse(TokenStream.isKeyword("if-else"));
        assertFalse(TokenStream.isKeyword("if else"));
        assertFalse(TokenStream.isKeyword("if else"));
        assertFalse(TokenStream.isKeyword("if else"));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        assertFalse(TokenStream.isKeyword("if ("));
        // Exception handling
        try {
            TokenStream.isKeyword(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsJSIdentifier() {
        // Basic functionality test
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("ABC"));
        assertTrue(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("1_23"));
        assertTrue(TokenStream.isJSIdentifier("_123"));
        assertTrue(TokenStream.isJSIdentifier("ab_c"));
        assertTrue(TokenStream.isJSIdentifier("A_Bc"));
        assertTrue(TokenStream.isJSIdentifier("ab_c1"));
        assertTrue(TokenStream.isJSIdentifier("A_Bc1"));
        // Edge cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("1_"));
        assertFalse(TokenStream.isJSIdentifier("_1"));
        assertFalse(TokenStream.isJSIdentifier("1_2"));
        assertFalse(TokenStream.isJSIdentifier("_1_2"));
        assertFalse(TokenStream.isJSIdentifier("1_2_"));
        assertFalse(TokenStream.isJSIdentifier("_1_2_"));
        assertFalse(TokenStream.isJSIdentifier("1_23"));
        assertFalse(TokenStream.isJSIdentifier("1_2 _3"));
        assertFalse(TokenStream.isJSIdentifier("1_2_3"));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        assertFalse(TokenStream.isJSIdentifier("1_2_3 "));
        // Exception handling
        try {
            TokenStream.isJSIdentifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}