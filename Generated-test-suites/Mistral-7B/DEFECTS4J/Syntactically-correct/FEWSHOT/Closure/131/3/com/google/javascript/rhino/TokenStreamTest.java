package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_FalseCases() {
 String[] testCases = { "", "ABC", "123", "this", "let", "const", "class", "var", "if", "else", "while", "do", "for", "switch", "case", "default", "break", "continue", "return", "goto", "throw", "try", "catch", "finally", "new", "delete", "import", "export", "package", "public", "private", "protected", "static", "final", "extends", "transient", "abstract", "interface", "implements", "instanceof", "super", "yield", "null", "true", "false", "NaN", "Infinity", "undefined" };

 for (String s : testCases) {
 assertFalse("Expected false for input: " + s, TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsKeyword_TrueCases() {
 String[] testCases = { "false", "null", "this", "void", "true", "break", "case", "catch", "class", "const", "continue", "default", "delete", "do", "else", "export", "extends", "false", "finally", "for", "function", "if", "implements", "import", "in", "instanceof", "interface", "new", "null", "package", "private", "protected", "return", "static", "super", "switch", "synchronized", "this", "throw", "throw", "true", "try", "typeof", "var", "void", "while", "with", "yield" };

 for (String s : testCases) {
 assertTrue("Expected true for input: " + s, TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsJSIdentifier_ValidCases() {
 String[] testCases = { "", "_abc123", "fooBar", "my_variable_name", "my_long_variable_name_with_underscores", "myVariableNameWithCamelCase", "MyVariableNameWithCamelCase", "My_Variable_Name_With_Camel_Case" };

 for (String s : testCases) {
 assertTrue("Expected true for input: " + s, TokenStream.isJSIdentifier(s));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidCases() {
 String[] testCases = { "123", "my-variable-name", "my variable name", "my variable name with spaces", "my_variable_name with spaces", "my variable name with spaces", "my_variable_name with trailing_space", "my_variable_name with trailing space" };

 for (String s : testCases) {
 assertFalse("Expected false for input: " + s, TokenStream.isJSIdentifier(s));
 }
 }
}