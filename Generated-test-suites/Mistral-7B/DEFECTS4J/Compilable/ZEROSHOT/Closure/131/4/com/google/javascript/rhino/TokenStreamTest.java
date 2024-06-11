package com.google.javascript.rhino;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("with"));
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("throws"));
 assertTrue(TokenStream.isKeyword("export default"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("ABC"));
 assertFalse(TokenStream.isKeyword("ABC_123"));
 assertFalse(TokenStream.isKeyword("__ABC"));
 assertFalse(TokenStream.isKeyword("ABC__"));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("ABCdef"));
 assertFalse(TokenStream.isKeyword("123ABC"));
 assertFalse(TokenStream.isKeyword("ABC123"));
 assertFalse(TokenStream.isKeyword("ABC_123"));
 assertFalse(TokenStream.isKeyword("__ABC"));
 assertFalse(TokenStream.isKeyword("ABC__"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("a"));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("123"));
 assertTrue(TokenStream.isJSIdentifier("ABC"));
 assertTrue(TokenStream.isJSIdentifier("ABC_123"));
 assertTrue(TokenStream.isJSIdentifier("__ABC"));
 assertTrue(TokenStream.isJSIdentifier("ABC__"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("ABCdef"));
 assertTrue(TokenStream.isJSIdentifier("123ABC"));
 assertTrue(TokenStream.isJSIdentifier("ABC123"));
 assertTrue(TokenStream.isJSIdentifier("ABC_123"));
 assertTrue(TokenStream.isJSIdentifier("__ABC"));
 assertTrue(TokenStream.isJSIdentifier("ABC__"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("123ABC"));
 assertFalse(TokenStream.isJSIdentifier("ABC123"));
 assertFalse(TokenStream.isJSIdentifier("ABC_123"));
 assertFalse(TokenStream.isJSIdentifier("__ABC"));
 assertFalse(TokenStream.isJSIdentifier("ABC__"));
 }
}