package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
 import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_Valid() {
 String[] keywords = new String[]{"abstract", "arguments", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "delete", "do", "double", "else", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"};

 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_Invalid() {
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("foo"));
 assertFalse(TokenStream.isKeyword("bar_baz"));
 assertFalse(TokenStream.isKeyword("myVar"));
 }

 @Test
 public void testIsJSIdentifier_Valid() {
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("foo"));
 assertTrue(TokenStream.isJSIdentifier("bar_baz"));
 assertTrue(TokenStream.isJSIdentifier("myVar"));
 }

 @Test
 public void testIsJSIdentifier_Invalid() {
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("foo bar"));
 assertFalse(TokenStream.isJSIdentifier("myVar_"));
 }
}