package com.google.javascript.rhino;

import static org.junit.Assert. assertTrue;
import static org.junit.Assert. assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_true_cases() {
 String[] keywords = new String[]{"if", "else", "for", "while", "new", "true", "false", "null", "this", "break", "continue", "return", "void", "int", "float", "double", "boolean", "long", "short", "byte", "char", "class", "package", "import", "interface", "extends", "implements", "instanceof", "static", "public", "private", "protected", "transient", "synchronized", "throws", "try", "catch", "finally", "default", "delete", "const", "final", "native", "abstract", "interface", "function", "export", "import", "export", "import", "import", "import"};

 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_false_cases() {
 String[] nonKeywords = new String[]{"foo", "bar", "baz", "qux", "quux", "corge", "widget", "gadget"};

 for (String nonKeyword : nonKeywords) {
 assertFalse(TokenStream.isKeyword(nonKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier_valid_cases() {
 String[] validIdentifiers = new String[]{"foo", "bar_baz", "qux$123", "_quux", "camelCase", "PascalCase", "IDENTIFIER_WITH_UNDERSCORES"};

 for (String validIdentifier : validIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(validIdentifier));
 }
 }

 @Test
 public void testIsJSIdentifier_invalid_cases() {
 String[] invalidIdentifiers = new String[]{"-foo", "foo-bar", "foo bar", "123foo", "foo123", "foo bar-baz", "foo_123", "foo%", "foo@", "foo!"};

 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
 }
 }
}