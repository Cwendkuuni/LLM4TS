package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_false() {
 String[] testCases = {"abcd", "efghijk", "12345", "!@#$%^&*()_+<>|{}=", "++--", "fooBar"};
 for (String name : testCases) {
 assertFalse("isKeyword(\"" + name + "\") should return false", TokenStream.isKeyword(name));
 }
 }

 @Test
 public void testIsKeyword_true() {
 String[] testCases = {"if", "else", "new", "var", "true", "false", "null", "this", "void", "false", "float", "short", "int", "long", "byte", "class", "function", "return", "break", "continue", "throw", "catch", "try", "finally", "export", "import", "package", "interface", "implements", "instanceof", "public", "private", "protected", "static", "abstract", "transient", "synchronized", "boolean", "false", "true"};
 for (String name : testCases) {
 assertTrue("isKeyword(\"" + name + "\") should return true", TokenStream.isKeyword(name));
 }
 }

 @Test
 public void testIsJSIdentifier_false() {
 String[] testCases = {"-foo", "foo-", "foo123", "foo-bar", "foo bar", "foo@bar.com", "foo$123"};
 for (String name : testCases) {
 assertFalse("isJSIdentifier(\"" + name + "\") should return false", TokenStream.isJSIdentifier(name));
 }
 }

 @Test
 public void testIsJSIdentifier_true() {
 String[] testCases = {"foo", "fooBar", "foo_123", "foo123", "foo@bar.com", "foo$123"};
 for (String name : testCases) {
 assertTrue("isJSIdentifier(\"" + name + "\") should return true", TokenStream.isJSIdentifier(name));
 }
 }
}