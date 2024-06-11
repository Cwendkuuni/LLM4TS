package com.google.javascript.rhino;
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
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));

        // Testing invalid keywords
        assertFalse(TokenStream.isKeyword("is"));
        assertFalse(TokenStream.isKeyword("let"));
        assertFalse(TokenStream.isKeyword("const")); // This one is a keyword but with a different meaning
        assertFalse(TokenStream.isKeyword("packageName"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("myIdentifier_with_$"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Testing valid JS identifiers
        assertTrue(TokenStream.isJSIdentifier("identifier"));
        assertTrue(TokenStream.isJSIdentifier("identifierWithNumbers_123"));
        assertTrue(TokenStream.isJSIdentifier("identifierWithUnderscore_and_numbers_123_"));
        assertTrue(TokenStream.isJSIdentifier("identifierWithCapitalLetter_Capital"));
        assertTrue(TokenStream.isJSIdentifier("identifierWithDollarSign_$"));

        // Testing invalid JS identifiers
        assertFalse(TokenStream.isJSIdentifier("identifier-with-dash"));
        assertFalse(TokenStream.isJSIdentifier("identifierWithSpace"));
        assertFalse(TokenStream.isJSIdentifier("identifierWithTab"));
        assertFalse(TokenStream.isJSIdentifier("identifierWithNewLine"));
        assertFalse(TokenStream.isJSIdentifier("identifierWithControlCharacter\b"));
    }
}