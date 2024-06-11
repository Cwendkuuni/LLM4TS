package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = { "abstract", "arguments", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger", "default", "delete", "do", "double", "else", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield" };

 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }

 String nonKeyword = "myVariable";
 assertFalse(TokenStream.isKeyword(nonKeyword));
 }

 @Test
 public void testIsJSIdentifier() {
 String[] jsIdentifiers = { "_abc123", "_$fooBar", "fooBar123", "foo_bar", "foo_bar_123", "foo_bar_123_", "foo_bar_123__", "foo_bar_123___", "foo_bar_123____", "foo_bar_123_____" };

 for (String identifier : jsIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }

 String nonJsIdentifier = "123foo";
 assertFalse(TokenStream.isJSIdentifier(nonJsIdentifier));
 }
}