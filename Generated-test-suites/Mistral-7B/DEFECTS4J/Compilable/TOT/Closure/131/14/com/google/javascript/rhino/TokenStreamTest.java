package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = {"if", "else", "while", "for", "switch", "case", "default",
 "try", "catch", "throw", "return", "new", "void", "true", "false", "null",
 "package", "import", "public", "protected", "static", "private", "transient",
 "interface", "implements", "extends", "abstract", "function", "class",
 "const", "final", "double", "int", "long", "short", "byte", "boolean",
 "this", "super", "goto", "break", "continue", "delete", "throw", "synchronized"};

 for (String keyword : keywords) {
 assertTrue(isKeyword(keyword));
 }

 String[] nonKeywords = {"foo", "bar", "baz", "qux", "quux", "corge", "garply",
 "grarply", "plugh", "snarf", "quux2", "blargh", "blibble", "quux3"};

 for (String nonKeyword : nonKeywords) {
 assertFalse(isKeyword(nonKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] validIdentifiers = {"identifier1", "_identifier2", "identifier_3",
 "identifier123", "identifier_123", "identifier_with_digits_only_54321",
 "identifier_with_digits_only_01234", "identifier_with_digits_only_1",
 "_identifier_with_digits_only_1", "identifierWithSpecialChars_!@#$%^&*()_+-={}[]|\\:\"<>?",
 "identifierWithSpecialChars_!@#$%^&*()_+-={}[]|\\:\"<>?._-"};

 for (String validIdentifier : validIdentifiers) {
 assertTrue(isJSIdentifier(validIdentifier));
 }

 String[] invalidIdentifiers = {"identifier-with-hyphen-in-the-middle", "identifier 123",
 "identifier with space in the middle", "identifier_with_space_at_the_beginning",
 "identifier_with_space_at_the_end", "identifierwith_underscore_at_the_beginning"};

 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(isJSIdentifier(invalidIdentifier));
 }
 }

 @Test
 public void testIsJSIdentifierWithEmptyString() {
 assertFalse(isJSIdentifier(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierWithNullString() {
 try {
 isJSIdentifier(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}