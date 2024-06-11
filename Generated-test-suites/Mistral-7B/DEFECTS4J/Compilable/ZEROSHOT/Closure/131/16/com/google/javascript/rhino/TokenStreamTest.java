package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertEquals(true, TokenStream.isKeyword("false"));
 assertEquals(true, TokenStream.isKeyword("goto"));
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("int"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("throw"));
 assertEquals(true, TokenStream.isKeyword("catch"));
 assertEquals(true, TokenStream.isKeyword("const"));
 assertEquals(true, TokenStream.isKeyword("float"));
 assertEquals(true, TokenStream.isKeyword("short"));
 assertEquals(true, TokenStream.isKeyword("super"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("native"));
 assertEquals(true, TokenStream.isKeyword("delete"));
 assertEquals(true, TokenStream.isKeyword("static"));
 assertEquals(true, TokenStream.isKeyword("public"));
 assertEquals(true, TokenStream.isKeyword("switch"));
 assertEquals(true, TokenStream.isKeyword("export"));
 assertEquals(true, TokenStream.isKeyword("package"));
 assertEquals(true, TokenStream.isKeyword("interface"));
 assertEquals(true, TokenStream.isKeyword("protected"));
 assertEquals(true, TokenStream.isKeyword("transient"));
 assertEquals(true, TokenStream.isKeyword("implements"));
 assertEquals(true, TokenStream.isKeyword("instanceof"));
 assertEquals(true, TokenStream.isKeyword("synchronized"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("abcd"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("_"));
 assertFalse(TokenStream.isKeyword("!"));
 assertFalse(TokenStream.isKeyword("~"));
 assertFalse(TokenStream.isKeyword("*"));
 assertFalse(TokenStream.isKeyword("+"));
 assertFalse(TokenStream.isKeyword("-"));
 assertFalse(TokenStream.isKeyword("/"));
 assertFalse(TokenStream.isKeyword(">"));
 assertFalse(TokenStream.isKeyword("<"));
 assertFalse(TokenStream.isKeyword("="));
 assertFalse(TokenStream.isKeyword("!"));
 assertFalse(TokenStream.isKeyword(">="));
 assertFalse(TokenStream.isKeyword("<="));
 assertFalse(TokenStream.isKeyword("=="));
 assertFalse(TokenStream.isKeyword("!="));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertEquals(true, TokenStream.isJSIdentifier("identifier1"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier_1"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier123"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier_123"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier123ABC"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier_123ABC"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier("abcd123"));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("_"));
 assertFalse(TokenStream.isJSIdentifier("!"));
 assertFalse(TokenStream.isJSIdentifier("~"));
 assertFalse(TokenStream.isJSIdentifier("*"));
 assertFalse(TokenStream.isJSIdentifier("+"));
 assertFalse(TokenStream.isJSIdentifier("-"));
 assertFalse(TokenStream.isJSIdentifier("/"));
 assertFalse(TokenStream.isJSIdentifier(">"));
 assertFalse(TokenStream.isJSIdentifier("<"));
 assertFalse(TokenStream.isJSIdentifier("="));
 assertFalse(TokenStream.isJSIdentifier("!"));
 assertFalse(TokenStream.isJSIdentifier(">="));
 assertFalse(TokenStream.isJSIdentifier("<="));
 assertFalse(TokenStream.isJSIdentifier("=="));
 assertFalse(TokenStream.isJSIdentifier("!="));
 }
}