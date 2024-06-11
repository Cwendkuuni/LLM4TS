package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = {
 "abstract", "arguments", "await", "boolean", "break", "byte", "case",
 "catch", "char", "class", "const", "continue", "crimethis", "default",
 "delete", "do", "double", "else", "export", "extends", "false",
 "final", "finally", "float", "for", "function", "goto", "if",
 "implements", "import", "in", "instanceof", "int", "interface",
 "let", "long", "native", "new", "null", "package", "private",
 "protected", "public", "return", "short", "static", "super", "switch",
 "synchronized", "this", "throw", "throws", "transient", "true",
 "try", "typeof", "var", "void", "volatile", "while", "with", "yield",
 "breakpoint", "eval", "id", "typeof", "console", "undefined"
 };

 for (String keyword : keywords) {
 assertTrue(TokenStream.isKeyword(keyword));
 }

 String[] nonKeywords = {
 "foo", "bar", "baz", "qux", "quux", "corge", "grault", "garply",
 "quark", "plugh", "xyzzy", "123", "456", "789", "ABC", "def", "ghi",
 "jkl", "mno", "pqr", "stu", "vwx", "wxy", "zap", "quuxquux"
 };

 for (String nonKeyword : nonKeywords) {
 assertFalse(TokenStream.isKeyword(nonKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] validIdentifiers = {
 "_foo", "_bar", "_baz", "_qux", "_quux", "_corge", "_grault", "_garply",
 "_quark", "_plugh", "_xyzzy", "_123", "_456", "_789", "_ABC", "_def", "_ghi",
 "_jkl", "_mno", "_pqr", "_stu", "_vwx", "_wxy", "_zap", "_quuxquux",
 "foo_bar", "foo_baz", "foo_qux", "foo_quux", "foo_corge", "foo_grault",
 "foo_garply", "foo_quark", "foo_plugh", "foo_xyzzy", "foo_123", "foo_456",
 "foo_789", "foo_ABC", "foo_def", "foo_ghi", "foo_jkl", "foo_mno", "foo_pqr",
 "foo_stu", "foo_vwx", "foo_wxy", "foo_zap", "foo_quuxquux", "_fooBar", "_fooBaz",
 "_fooQux", "_fooQuux", "_fooCorge", "_fooGrault", "_fooGarply", "_fooQuark",
 "_fooPlugh", "_fooXyzzy", "_foo123", "_foo456", "_foo789", "_fooABC", "_fooDef",
 "_fooGhi", "_fooJkl", "_fooMno", "_fooPqr", "_fooStu", "_fooVwx", "_fooWxy",
 "_fooZap", "_fooQuuxquux"
 };

 for (String identifier : validIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }

 String[] invalidIdentifiers = {
 "123foo", "foo123", "foo-bar", "foo bar", "foo @bar", "foo_123",
 "foo_bar_baz", "foo_bar-baz", "foo_bar @baz", "foo_bar_qux", "foo_qux_quux",
 "foo qux quux", "foo qux_quux", "foo quxQuux", "foo Quux quux", "foo Quux_quux",
 "foo QuuxQuux"
 };

 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
 }
 }
}