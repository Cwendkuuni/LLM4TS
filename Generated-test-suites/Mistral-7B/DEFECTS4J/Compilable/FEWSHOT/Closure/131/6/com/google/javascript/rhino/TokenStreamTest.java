package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_BasicKeywords() {
 for (String keyword : new String[]{"if", "else", "else if", "while", "for", "return", "void", "break", "continue", "switch", "case", "default", "false", "true", "null", "this", "super", "class", "int", "float", "double", "byte", "short", "long", "char", "boolean", "package", "import", "public", "private", "protected", "static", "final", "const", "new", "delete", "throws", "try", "catch", "finally", "breakpoint", "export", "extends", "implements", "interface", "transient", "synchronized"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_InvalidStrings() {
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(null));
 assertFalse(TokenStream.isKeyword("invalid"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("abcd"));
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 for (String identifier : new String[]{"myVariable", "My_Variable", "_myVariable", "my_Variable_123", "myVariable_abcd"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(null));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("my-variable"));
 assertFalse(TokenStream.isJSIdentifier("my variable"));
 }
}