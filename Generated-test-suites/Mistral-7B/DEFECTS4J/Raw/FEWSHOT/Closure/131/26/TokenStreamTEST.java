package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_trueCases() {
 for (String keyword : new String[]{"if", "for", "return", "new", "int", "double", "void", "class", "while", "else", "false", "true", "break", "switch", "case", "throw", "try", "catch", "finally", "package", "public", "protected", "private", "static", "import", "interface", "extends", "implements", "native", "abstract", "float", "short", "byte", "long", "void", "super", "this", "null", "true", "false", "delete", "const", "final", "debugger", "function", "new", "continue", "instanceof", "transient", "synchronized", "export", "import", "from", "as"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_falseCases() {
 for (String s : new String[]{"foo", "bar", "baz", "qux", "quux", "corge", "grault", "garply", "waldo", "wibble", "wobble", "quux2", "quux3", "quux4", "quux5", "_foo", "foo_", "fooBar", "foo-bar", "foo_bar", "foo_baz", "foo_qux", "foo-qux", "foo_qux_baz", "foo_qux_qux", "foo_qux_qux_baz", "foo_qux_qux_qux"}) {
 assertFalse(TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsJSIdentifier_validIdentifiers() {
 for (String identifier : new String[]{"foo", "bar", "baz", "qux", "quuz", "_foo", "foo_bar", "foo-bar", "foo_baz", "foo_qux"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_invalidIdentifiers() {
 for (String s : new String[]{"1foo", "foo1", "foo-1", "foo_1", "foo123", "foo-123", "foo_123", "foo 123", "foo_123 ", "foo_1 23", "foo_1 23 ", "foo_1 23", "foo_1 23", "foo_1 23", "foo_1 23"}) {
 assertFalse(TokenStream.isJSIdentifier(s));
 }
 }
}