package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_empty() {
 assertTrue(TokenStream.isKeyword(""));
 }

 @Test
 public void testIsKeyword_false() {
 assertFalse(TokenStream.isKeyword("foo"));
 }

 @Test
 public void testIsKeyword_true() {
 for (String keyword : new String[]{"if", "else", "else if", "new", "false", "true", "null", "void", "this", "return", "int", "float", "short", "long", "byte", "package", "public", "static", "class", "interface", "extends", "implements", "private", "protected", "transient", "abstract", "final", "const", "double", "boolean", "import", "export", "typeof", "delete", "throw", "catch", "try", "finally", "break", "continue", "debugger", "synchronized"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsJSIdentifier_valid() {
 for (String identifier : new String[]{"_foo", "fooBar123", "my_valid_identifier", "identifierWithDigits_123"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_invalid() {
 for (String identifier : new String[]{"123_foo", "foo-bar", "foo bar", "foo bar 123", "foo@bar.com"}) {
 assertFalse(TokenStream.isJSIdentifier(identifier));
 }
 }
}