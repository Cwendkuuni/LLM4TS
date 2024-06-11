package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_ShortStrings() {
 for (String keyword : new String[]{"if", "else", "else if", "new", "true", "false"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_LongStrings() {
 for (String keyword : new String[]{"abstract", "continue", "debugger", "function", "interface", "package", "protected", "synchronized", "transient", "volatile", "implements", "instanceof", "import", "export", "this", "super", "yield", "class", "const", "let", "var", "delete", "return", "throws", "try", "catch", "finally"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_InvalidStrings() {
 for (String s : new String[]{"", "123", "abcd_efgh", "ABC_def", "ab$cd", "is this a keyword?"}) {
 assertFalse(TokenStream.isKeyword(s));
 }
 }

 @Test
 public void testIsJSIdentifier_ValidStrings() {
 for (String identifier : new String[]{"foo", "bar", "myVar321", "_myVar_321", "myFunction", "_myFunction_"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidStrings() {
 for (String identifier : new String[]{"", "123", "my-var321", "my_var_321", "myFunction_", "is_this_a_identifier?"}) {
 assertFalse(TokenStream.isJSIdentifier(identifier));
 }
 }
}