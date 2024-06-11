package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Test true cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("yield"));

 // Test false cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("abcd123"));
 assertFalse(TokenStream.isKeyword("abcd123_"));
 assertFalse(TokenStream.isKeyword("abcd123_1"));
 assertFalse(TokenStream.isKeyword("abcd123_123"));
 assertFalse(TokenStream.isKeyword("ABC"));
 assertFalse(TokenStream.isKeyword("ABCD123"));
 assertFalse(TokenStream.isKeyword("ABCD123_"));
 assertFalse(TokenStream.isKeyword("ABCD123_1"));
 assertFalse(TokenStream.isKeyword("ABCD123_123"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_1"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_1"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_1"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_123"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_123_"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_123_1"));
 assertFalse(TokenStream.isKeyword("ABCD123_123_123_123_123_123"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Test true cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("_abc"));
 assertTrue(TokenStream.isJSIdentifier("abc_1"));
 assertTrue(TokenStream.isJSIdentifier("abc_123"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_1"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_1"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_1"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_123"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_123_"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_123_1"));
 assertTrue(TokenStream.isJSIdentifier("abc_123_123_123_123_123"));

 // Test false cases
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("123_"));
 assertFalse(TokenStream.isJSIdentifier("123_1"));
 assertFalse(TokenStream.isJSIdentifier("123_123"));
 assertFalse(TokenStream.isJSIdentifier("123_123_"));
 assertFalse(TokenStream.isJSIdentifier("123_123_1"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_1"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_1"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_123"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_123_"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_123_1"));
 assertFalse(TokenStream.isJSIdentifier("123_123_123_123_123_123"));
 }
}