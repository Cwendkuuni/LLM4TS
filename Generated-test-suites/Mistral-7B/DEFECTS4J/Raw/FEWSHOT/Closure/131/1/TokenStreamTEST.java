package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_SimpleKeywords() {
 for (String keyword : new String[]{"if", "int", "new", "else", "return", "for", "void", "while", "var", "this", "with", "break", "throw", "case", "catch", "try", "export", "class", "false", "true", "null", "boolean", "package", "abstract", "continue", "interface", "implements", "instanceof", "transient", "protected", "synchronized", "default", "finally"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_MultiCharKeywords() {
 for (String keyword : new String[]{"else if", "in", "instanceof", "typeof", "this.super", "Object.prototype"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_StringLiterals() {
 assertFalse(TokenStream.isKeyword("hello"));
 }

 @Test
 public void testIsKeyword_ReservedWords() {
 assertFalse(TokenStream.isKeyword("const"));
 assertFalse(TokenStream.isKeyword("let"));
 assertFalse(TokenStream.isKeyword("const")); //yes, it's repeated intentionally
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 for (String identifier : new String[]{"identifier1", "identifier_2", "_identifier3", "identifier_with_number_123", "identifierWithDigits_123"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 for (String identifier : new String[]{"1identifier", "identifier123", "identifier-with-hyphens", "identifier with spaces", "identifier tab"}) {
 assertFalse(TokenStream.isJSIdentifier(identifier));
 }
 }
}