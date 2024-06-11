package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_falseCases() {
 String[] testCases = {
 "abc", "def", "123", "xyz", "!@#$%^&*()_+-=[]{}|\\;:'\",.<>?",
 "ifx", "forx", "let x", "const x", "class X", "package x", "abstract x"
 };
 for (String name : testCases) {
 assertFalse("'" + name + "' should not be a keyword", TokenStream.isKeyword(name));
 }
 }

 @Test
 public void testIsKeyword_trueCases() {
 String[] testCases = {
 "if", "else", "else if", "for", "while", "switch", "case", "default",
 "try", "catch", "throw", "return", "break", "continue", "goto",
 "true", "false", "null", "void", "boolean", "byte", "short", "int",
 "long", "float", "double", "char", "String", "class", "interface",
 "package", "import", "export", "public", "protected", "static",
 "transient", "abstract", "final", "const", "native", "syncronized",
 "this", "super"
 };
 for (String name : testCases) {
 assertTrue("'" + name + "' should be a keyword", TokenStream.isKeyword(name));
 }
 }

 @Test
 public void testIsJSIdentifier_validCases() {
 String[] testCases = {
 "_abc", "ab_c", "_abC", "AbC", "ab_123", "_123ab", "_1_2_3", "_abc_def_123",
 "_ab_c_123_def_456", "_ab_c_123_def_456_xyz"
 };
 for (String identifier : testCases) {
 assertTrue("'" + identifier + "' is a valid identifier", TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_invalidCases() {
 String[] testCases = {
 "123_ab", "_1a_b", "_ab1", "_ab-c", "_ab*c", "_ab/c", "_ab+c", "_ab%c", "_ab^c", "_ab&c", "_ab|c", "_ab#c", "_ab$c", "_ab@c", "_ab[c", "_ab]c", "_ab{c", "_ab}c"
 };
 for (String identifier : testCases) {
 assertFalse("'" + identifier + "' is an invalid identifier", TokenStream.isJSIdentifier(identifier));
 }
 }
}