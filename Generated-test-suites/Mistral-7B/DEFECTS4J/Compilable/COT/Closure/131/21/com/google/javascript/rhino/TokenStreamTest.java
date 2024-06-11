package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

 private static final String[] KEYWORDS = {
 "abstract", "arguments", "as", "await", "boolean", "break", "byte", "case",
 "catch", "class", "const", "continue", "debugger", "default", "delete",
 "do", "double", "else", "export", "extends", "false", "final", "finally",
 "for", "function", "goto", "if", "implements", "import", "in", "instanceof",
 "int", "interface", "let", "long", "native", "new", "null", "package",
 "private", "protected", "public", "return", "short", "static", "super",
 "switch", "synchronized", "this", "throw", "throws", "transient", "true",
 "try", "typeof", "var", "void", "volatile", "while", "with", "yield"
 };

 @Test
 public void testIsKeyword() {
 for (String keyword : KEYWORDS) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsNotKeyword() {
 assertFalse(TokenStream.isKeyword("myVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // basic test
 assertTrue(TokenStream.isJSIdentifier("myVariable"));

 // edge cases
 assertTrue(TokenStream.isJSIdentifier("my_variable"));
 assertTrue(TokenStream.isJSIdentifier("myVariable123"));
 assertTrue(TokenStream.isJSIdentifier("myVariable_123"));
 assertTrue(TokenStream.isJSIdentifier("my_variable_123"));

 // exceptions
 assertFalse(TokenStream.isJSIdentifier("my variable"));
 assertFalse(TokenStream.isJSIdentifier("123myVariable"));
 assertFalse(TokenStream.isJSIdentifier("myVariable-123"));
 assertFalse(TokenStream.isJSIdentifier("myVariable@gmail.com"));
}
}