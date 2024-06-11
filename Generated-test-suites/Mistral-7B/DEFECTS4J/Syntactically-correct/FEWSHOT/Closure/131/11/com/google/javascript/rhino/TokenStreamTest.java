package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_FalseCases() {
 String[] falseCases = new String[] {"abcd", "123", "ABC123", "myVar", "myFunction"};
 for (String s : falseCases) {
 assertFalse("'" + s + "' is not a keyword", TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsKeyword_TrueCases() {
 String[] trueCases = new String[] {"if", "else", "new", "for", "break", "return", "void", "int", "boolean", "package", "abstract", "continue", "interface", "implements", "class", "extends", "this", "super", "public", "static", "private", "protected", "transient", "synchronized"};
 for (String s : trueCases) {
 assertTrue("'" + s + "' is a keyword", TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsJSIdentifier_FalseCases() {
 String[] falseCases = new String[] {"1a", "ab-cd", "my_var", "my-function", "my function", "my variable", "my-variable", "my variable_", "my variable__"};
 for (String s : falseCases) {
 assertFalse("'" + s + "' is not a valid JS identifier", TokenStream.isJSIdentifier(s));
 }
 }

 @Test
 public void testIsJSIdentifier_TrueCases() {
 String[] trueCases = new String[] {"myVariable", "myFunction", "my_function", "myFunction_", "myFunction__", "myVariable_1", "myVariable_123", "myVariable_123abc"};
 for (String s : trueCases) {
 assertTrue("'" + s + "' is a valid JS identifier", TokenStream.isJSIdentifier(s));
 }
 }
}