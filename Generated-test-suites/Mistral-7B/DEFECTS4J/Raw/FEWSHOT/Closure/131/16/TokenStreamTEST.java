package com.google.javascript.rhino;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 final String[] keywords = {
 "abstract", "arguments", "await", "boolean", "break", "byte", "case", "catch",
 "char", "class", "const", "continue", "debugger", "default", "delete", "do",
 "double", "else", "export", "extends", "false", "final", "finally", "float",
 "for", "function", "goto", "if", "implements", "import", "in", "instanceof",
 "int", "interface", "let", "long", "native", "new", "null", "package",
 "private", "protected", "public", "return", "short", "super", "switch",
 "synchronized", "this", "throw", "throws", "transient", "true", "try",
 "typeof", "var", "void", "volatile", "with", "while", "yield", "static",
 "const", "get", "set"
 };

 for (String keyword : keywords) {
 assertEquals(true, TokenStream.isKeyword(keyword));
 }

 String nonKeyword = "myVariable";
 assertEquals(false, TokenStream.isKeyword(nonKeyword));
 }

 @Test
 public void testIsJSIdentifier() {
 final String[] validIdentifiers = {
 "_", "_$", "_$ABC", "abracadabra", "123", "abcDef", "AbCdEf_123", "AbCdEf_123_",
 "abc_def_123", "AbCdEf123", "AbCdEf_123", "AbCdEf_123_", "ABCdEf123"
 };

 for (String identifier : validIdentifiers) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }

 String invalidIdentifier1 = "123_AbC";
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier1));

 String invalidIdentifier2 = "my_variable_name_is_too_long";
 assertFalse(TokenStream.isJSIdentifier(invalidIdentifier2));
 }
}