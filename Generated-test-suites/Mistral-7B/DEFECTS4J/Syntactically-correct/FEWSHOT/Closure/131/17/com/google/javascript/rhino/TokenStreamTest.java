package com.google.javascript.rhino.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = new String[]{"if", "else", "for", "while", "switch", "case", "default", "break", "continue", "return", "boolean", "byte", "short", "int", "long", "float", "double", "null", "void", "true", "false", "this", "super", "new", "delete", "class", "package", "import", "export", "public", "protected", "private", "static", "transient", "final", "const", "abstract", "interface", "implements", "extends", "throws", "synchronized", "try", "catch", "finally", "debugger", "function", "native", "const", "let", "const", "var", "export", "import", "yield"};
 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }

 String[] nonKeywords = new String[]{"foo", "bar", "baz", "qux", "quux", "corgi"};
 for (String nonKeyword : nonKeywords) {
 assertFalse(TokenStream.isKeyword(nonKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] identifiers = new String[]{"foo", "bar_baz", "camelCase", "FOO_BAR", "_foo", "foo_bar_baz", "fooBar"};
 for (String identifier : identifiers) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }

 String[] invalidIdentifiers = new String[]{"foo_", "foo-bar", "foo:bar", "foo.bar"};
 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
 }
 }
}