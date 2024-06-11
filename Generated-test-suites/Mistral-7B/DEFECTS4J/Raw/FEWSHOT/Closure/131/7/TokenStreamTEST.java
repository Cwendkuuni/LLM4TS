package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_EmptyString() {
 assertFalse(TokenStream.isKeyword(""));
 }

 @Test
 public void testIsKeyword_ValidKeywords() {
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("else if"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));
 }

 @Test
 public void testIsKeyword_InvalidIdentifiers() {
 assertFalse(TokenStream.isKeyword("myVar"));
 assertFalse(TokenStream.isKeyword("myFunction"));
 assertFalse(TokenStream.isKeyword("myClass"));
 assertFalse(TokenStream.isKeyword("myPackage"));
 }

 @Test
 public void testIsKeyword_LongIdentifiers() {
 assertTrue(TokenStream.isKeyword("myLongIdentifier"));
 assertTrue(TokenStream.isKeyword("myVeryLongIdentifierThatMightContainDigitsOrSpecialCharsOrBoth"));
 }

 @Test
 public void testIsKeyword_ReservedWords() {
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("arguments"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("byte"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("char"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("debugger"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("do"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("goto"));
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("long"));
 assertTrue(TokenStream.isKeyword("native"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("short"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("synchronized"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("throws"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("volatile"));
 }

 @Test
 public void testIsJSIdentifier_EmptyString() {
 assertFalse(TokenStream.isJSIdentifier(""));
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 assertTrue(TokenStream.isJSIdentifier("myVar"));
 assertTrue(TokenStream.isJSIdentifier("myFunction"));
 assertTrue(TokenStream.isJSIdentifier("myClass"));
 assertTrue(TokenStream.isJSIdentifier("myPackage"));
 assertTrue(TokenStream.isJSIdentifier("myLongIdentifier"));
 assertTrue(TokenStream.isJSIdentifier("myVeryLongIdentifierThatMightContainDigitsOrSpecialCharsOrBoth"));
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 assertFalse(TokenStream.isJSIdentifier("1myVar"));
 assertFalse(TokenStream.isJSIdentifier("my-var"));
 assertFalse(TokenStream.isJSIdentifier("myVar@"));
 assertFalse(TokenStream.isJSIdentifier("myVar#"));
 assertFalse(TokenStream.isJSIdentifier("myVar%"));
 assertFalse(TokenStream.isJSIdentifier("myVar_"));
 assertFalse(TokenStream.isJSIdentifier("myVar$"));
 assertFalse(TokenStream.isJSIdentifier("myVar!"));
 assertFalse(TokenStream.isJSIdentifier("myVar&"));
 assertFalse(TokenStream.isJSIdentifier("myVar'"));
 assertFalse(TokenStream.isJSIdentifier("myVar( "));
 assertFalse(TokenStream.isJSIdentifier("myVar)"));
 assertFalse(TokenStream.isJSIdentifier("myVar[ "));
 assertFalse(TokenStream.isJSIdentifier("myVar]"));
 assertFalse(TokenStream.isJSIdentifier("myVar{ "));
 assertFalse(TokenStream.isJSIdentifier("myVar}"));
 assertFalse(TokenStream.isJSIdentifier("myVar,"));
 }
}