package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_SimpleKeywords() {
 for (String keyword : new String[]{"if", "else", "for", "while", "new", "true", "false", "break", "return", "class", "private", "public", "static", "package", "import", "export", "export default", "native", "const", "let", "const", "function", "abstract", "interface", "extends", "implements", "this", "super", "yield", "var", "void", "null", "true", "false", "try", "catch", "finally", "debugger", "with", "in", "delete", "throws"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_InvalidStrings() {
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(null));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("fooBar"));
 }

 @Test
 public void testIsKeyword_PartialMatches() {
 assertFalse(TokenStream.isKeyword("ifElse"));
 assertFalse(TokenStream.isKeyword("forLoop"));
 assertFalse(TokenStream.isKeyword("isKeyword"));
 assertFalse(TokenStream.isKeyword("isJSIdentifier"));
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 for (String identifier : new String[]{"identifier1", "Identifier_1", "identifier_1", "_identifier1", "Identifier1_", "_Identifier1", "identifier1_", "_identifier_1", "Identifier1", "_Identifier1_", "_Identifier1_", "_Identifier1", "_identifier1", "_identifier_1", "_Identifier_1"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(null));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("ifElse"));
 assertFalse(TokenStream.isJSIdentifier("forLoop"));
 assertFalse(TokenStream.isJSIdentifier("isKeyword"));
 assertFalse(TokenStream.isJSIdentifier("isJSIdentifier"));
 assertFalse(TokenStream.isJSIdentifier("identifier1_1"));
 }
}