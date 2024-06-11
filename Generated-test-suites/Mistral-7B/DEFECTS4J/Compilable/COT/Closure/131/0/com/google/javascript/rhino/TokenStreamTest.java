package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

 private static final String[] KEYWORDS = {
   "abstract", "arguments", "await", "boolean", "break", "byte", "case",
   "catch", "char", "class", "const", "continue", "debugger", "default",
   "delete", "do", "double", "else", "enum", "eval", "export", "extends",
   "false", "final", "finally", "float", "for", "function", "goto",
   "if", "implements", "import", "in", "instanceof", "int", "interface",
   "let", "long", "native", "new", "null", "package", "private", "protected",
   "public", "return", "short", "super", "switch", "synchronized", "this",
   "throw", "throws", "transient", "true", "try", "typeof", "var", "void",
   "volatile", "while", "with", "yield"
 };

 @Test
 public void testIsKeyword() {
   for (String name : KEYWORDS) {
     assertTrue(TokenStream.isKeyword(name));
   }
 }

 @Test
 public void testIsKeyword_False() {
   assertFalse(TokenStream.isKeyword("invalidKeyword"));
 }

 @Test
 public void testIsJSIdentifier() {
   String validIdentifier = "_myIdentifier";
   assertTrue(TokenStream.isJSIdentifier(validIdentifier));

   String validIdentifierWithDigits = "_myIdentifier123";
   assertTrue(TokenStream.isJSIdentifier(validIdentifierWithDigits));

   String validIdentifierWithDollar = "_myIdentifier$";
   assertTrue(TokenStream.isJSIdentifier(validIdentifierWithDollar));

   String validIdentifierWithUnderscore = "_my_identifier";
   assertTrue(TokenStream.isJSIdentifier(validIdentifierWithUnderscore));

   String validIdentifierFirstCharacterIsDigit = "1_myIdentifier";
   assertTrue(TokenStream.isJSIdentifier(validIdentifierFirstCharacterIsDigit));

   String validIdentifierWithCapitalLetter = "MyIdentifier";
   assertTrue(TokenStream.isJSIdentifier(validIdentifierWithCapitalLetter));

   assertFalse(TokenStream.isJSIdentifier(""));
   assertFalse(TokenStream.isJSIdentifier("myIdentifier "));
   assertFalse(TokenStream.isJSIdentifier("myIdentifier-"));
   assertFalse(TokenStream.isJSIdentifier("myIdentifier!"));
   assertFalse(TokenStream.isJSIdentifier("myIdentifier@"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeyword_Null() {
   TokenStream.isKeyword(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeyword_EmptyString() {
   TokenStream.isKeyword("");
 }

 @Test
 public void testIsKeyword_UpperCase() {
   String upperCaseKeyword = "Abstract";
   assertTrue(TokenStream.isKeyword(upperCaseKeyword));
 }

 @Test
 public void testIsKeyword_MultipleLetters() {
   String multipleLettersKeyword = "Long";
   assertTrue(TokenStream.isKeyword(multipleLettersKeyword));
 }

 @Test
 public void testIsKeyword_ContainsDigits() {
   String containsDigitsKeyword = "Byte";
   assertTrue(TokenStream.isKeyword(containsDigitsKeyword));
 }

 @Test
 public void testIsKeyword_ContainsUnderScore() {
   String containsUnderScoreKeyword = "goto";
   assertTrue(TokenStream.isKeyword(containsUnderScoreKeyword));
   }

 @Test
 public void testIsKeyword_ContainsDigitsAndUnderScore() {
   String containsDigitsAndUnderScoreKeyword = "typeof";
   assertTrue(TokenStream.isKeyword(containsDigitsAndUnderScoreKeyword));
 }
}