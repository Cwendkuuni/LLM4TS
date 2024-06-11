package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertEquals(true, TokenStream.isKeyword("var"));
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("for"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("continue"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("function"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("export"));
 assertEquals(true, TokenStream.isKeyword("import"));
 assertEquals(true, TokenStream.isKeyword("package"));
 assertEquals(true, TokenStream.isKeyword("interface"));
 assertEquals(true, TokenStream.isKeyword("extends"));
 assertEquals(true, TokenStream.isKeyword("implements"));
 assertEquals(true, TokenStream.isKeyword("native"));
 assertEquals(true, TokenStream.isKeyword("static"));
 assertEquals(true, TokenStream.isKeyword("public"));
 assertEquals(true, TokenStream.isKeyword("private"));
 assertEquals(true, TokenStream.isKeyword("protected"));
 assertEquals(true, TokenStream.isKeyword("this"));
 assertEquals(true, TokenStream.isKeyword("super"));
 assertEquals(true, TokenStream.isKeyword("yield"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("1"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("123ab"));
 assertFalse(TokenStream.isKeyword("ab123"));
 assertFalse(TokenStream.isKeyword("abcd"));
 assertFalse(TokenStream.isKeyword("ABCD"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertEquals(true, TokenStream.isJSIdentifier(""));
 assertEquals(true, TokenStream.isJSIdentifier("a"));
 assertEquals(true, TokenStream.isJSIdentifier("_"));
 assertEquals(true, TokenStream.isJSIdentifier("ab"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_123"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_123_"));
 assertEquals(true, TokenStream.isJSIdentifier("abC"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_C"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_C123"));
 assertEquals(true, TokenStream.isJSIdentifier("ab_C123_"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("1"));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("123ab"));
 assertFalse(TokenStream.isJSIdentifier("ab123"));
 assertFalse(TokenStream.isJSIdentifier("ab123_"));
 assertFalse(TokenStream.isJSIdentifier("_123"));
 assertFalse(TokenStream.isJSIdentifier("_123_"));
 }
}