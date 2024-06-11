package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_FalseCases() {
 String[] testCases = new String[]{"Abc123", "abcd", "123", "AnormalIdentifier", "my_variable_name", "true", "false", "null", "function expression"};
 for (String testCase : testCases) {
 assertFalse("'" + testCase + "' should not be a keyword", TokenStream.isKeyword(testCase));
 }
 }

 @Test
 public void testIsKeyword_TrueCases() {
 String[] testCases = new String[]{"if", "else", "for", "while", "break", "return", "void", "class", "public", "private", "protected", "interface", "implements", "extends", "throw", "catch", "try", "finally", "package", "static", "new", "true", "false", "null", "boolean", "int", "float", "double", "long", "byte", "short", "char", "void", "function", "goto", "case", "default", "continue", "debugger", "abstract", "final", "native", "const", "super", "this", "delete", "in", "instanceof", "return", "throw", "case", "default", "yield", "export", "import", "from", "let", "const", "var", "export default", "async", "await", "class", "extends", "implements", "interface", "implements", "public", "private", "protected", "static", "final", "native", "const", "export", "import", "from", "require", "require", "require"};
 for (String testCase : testCases) {
 assertTrue("'" + testCase + "' should be a keyword", TokenStream.isKeyword(testCase));
 }
}
}