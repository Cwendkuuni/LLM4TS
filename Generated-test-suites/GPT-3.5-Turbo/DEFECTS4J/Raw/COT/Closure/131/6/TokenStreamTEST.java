import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertFalse(TokenStream.isKeyword("test"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("a123"));
        assertTrue(TokenStream.isJSIdentifier("_abc"));
        assertTrue(TokenStream.isJSIdentifier("$xyz"));
        assertTrue(TokenStream.isJSIdentifier("Aaa"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("@abc"));
        assertFalse(TokenStream.isJSIdentifier("abc!"));
        assertFalse(TokenStream.isJSIdentifier("ab c"));
    }
}