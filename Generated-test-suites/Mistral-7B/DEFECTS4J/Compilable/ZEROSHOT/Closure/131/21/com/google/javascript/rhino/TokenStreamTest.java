package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing valid keywords
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("arguments"));
 assertTrue(TokenStream.isKeyword("async"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("debugger"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("do"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("final"));
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
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("volatile"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("with"));

 // Testing invalid keywords
 assertFalse(TokenStream.isKeyword("invalidKeyword"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing valid JS identifiers
 assertTrue(TokenStream.isJSIdentifier("identifier1"));
 assertTrue(TokenStream.isJSIdentifier("identifier2_with_underscore"));
 assertTrue(TokenStream.isJSIdentifier("identifier3withDigits"));
 assertTrue(TokenStream.isJSIdentifier("identifier4WithCamelCase"));

 // Testing invalid JS identifiers
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier1"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier2_with_underscore@"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier3withDigits!"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier4WithCamelCase%"));
 }
}