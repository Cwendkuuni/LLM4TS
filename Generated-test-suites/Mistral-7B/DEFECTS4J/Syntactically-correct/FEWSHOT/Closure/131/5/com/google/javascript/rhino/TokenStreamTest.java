package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = new String[]{"if", "else", "else if", "new", "for", "while", "return", "int", "float", "double", "boolean", "true", "false", "null", "void", "this", "super", "break", "continue", "switch", "case", "default", "goto", "try", "catch", "finally", "throw", "class", "interface", "extends", "implements", "package", "public", "private", "protected", "static", "final", "abstract", "const", "syncronized"};
 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }

 String[] nonKeywords = new String[]{"foo", "bar", "baz", "qux", "quux", "corge"};
 for (String nonKeyword : nonKeywords) {
 assertFalse(TokenStream.isKeyword(nonKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] validIdentifiers = new String[]{"foo", "bar_baz", "qux_quux", "corge_123", "_foo", "foo_bar", "fooBar"};
 for (String validIdentifier : validIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(validIdentifier));
 }

 String[] invalidIdentifiers = new String[]{"foo!", "foo@", "foo$", "foo%", "foo^", "foo&", "foo#", "foo*", "foo(", "foo)", "foo[", "foo]", "foo{", "foo}", "foo|", "foo\\", "foo\"", "foo'", "foo<", "foo>", "foo:"};
 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
 }
 }
}