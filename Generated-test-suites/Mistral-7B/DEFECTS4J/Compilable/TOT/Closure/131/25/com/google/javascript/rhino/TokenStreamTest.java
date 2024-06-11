package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] testCases = new String[]{"if", "else", "while", "for", "switch", "case", "default", "break", "continue", "return", "boolean", "false", "true", "null", "void", "byte", "int", "short", "long", "float", "double", "char", "String", "class", "interface", "extends", "implements", "package", "public", "private", "protected", "static", "transient", "abstract", "final", "native", "synchronized", "throws", "import", "export"};
 for (String s : testCases) {
 boolean expected = isKeyword(s);
 assertTrue("Expected '" + s + "' to be a keyword.", expected);
 }

 String[] falseTestCases = new String[]{"abc", "123", "def", "!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~`"};
 for (String s : falseTestCases) {
 boolean expected = isKeyword(s);
 assertFalse("Expected '" + s + "' not to be a keyword.", expected);
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] testCases = new String[]{"identifier1", "identifier2", "identifier3", "identifier4", "identifier5"};
 for (String s : testCases) {
 boolean expected = isJSIdentifier(s);
 assertTrue("Expected '" + s + "' to be a valid JavaScript identifier.", expected);
 }

 String[] invalidTestCases = new String[]{"invalid1", "invalid2", "invalid3", "invalid4", "invalid5"};
 for (String s : invalidTestCases) {
 boolean expected = isJSIdentifier(s);
 assertFalse("Expected '" + s + "' not to be a valid JavaScript identifier.", expected);
 }
}

 @Test
 public void testIsJSIdentifier_EmptyString() {
 boolean expected = isJSIdentifier("");
 assertFalse("Expected an empty string not to be a valid JavaScript identifier.", expected);
}

 @Test
 public void testIsJSIdentifier_SingleCharacter() {
 for (char c = 'A'; c <= 'Z'; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertTrue("Expected character '" + c + "' to be a valid JavaScript identifier.", expected);
 }

 for (char c = 'a'; c <= 'z'; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertTrue("Expected character '" + c + "' to be a valid JavaScript identifier.", expected);
 }

 for (char c = '0'; c <= '9'; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertTrue("Expected digit '" + c + "' to be a valid JavaScript identifier.", expected);
 }

 for (char c = '_'; c <= '_'; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertTrue("Expected underscore '_' to be a valid JavaScript identifier.", expected);
 }

 for (char c = '\\'; c <= '\\'; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertFalse("Expected backslash '\\' not to be a valid JavaScript identifier.", expected);
 }

 for (char c = ' '; c <= ' '; c++) {
 String s = Character.toString(c);
 boolean expected = isJSIdentifier(s);
 assertFalse("Expected space ' ' not to be a valid JavaScript identifier.", expected);
 }
}

 @Test
 public void testIsKeyword_EmptyString() {
 boolean expected = isKeyword("");
 assertFalse("Expected an empty string not to be a keyword.", expected);
}
}