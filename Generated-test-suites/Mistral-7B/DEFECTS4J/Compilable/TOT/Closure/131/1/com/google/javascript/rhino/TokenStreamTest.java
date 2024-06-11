package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing valid keywords
 assertTrue(isKeyword("break"));
 assertTrue(isKeyword("case"));
 assertTrue(isKeyword("catch"));
 assertTrue(isKeyword("class"));
 assertTrue(isKeyword("const"));
 assertTrue(isKeyword("continue"));
 assertTrue(isKeyword("default"));
 assertTrue(isKeyword("delete"));
 assertTrue(isKeyword("do"));
 assertTrue(isKeyword("else"));
 assertTrue(isKeyword("export"));
 assertTrue(isKeyword("extends"));
 assertTrue(isKeyword("false"));
 assertTrue(isKeyword("finally"));
 assertTrue(isKeyword("for"));
 assertTrue(isKeyword("function"));
 assertTrue(isKeyword("goto"));
 assertTrue(isKeyword("if"));
 assertTrue(isKeyword("import"));
 assertTrue(isKeyword("in"));
 assertTrue(isKeyword("instanceof"));
 assertTrue(isKeyword("int"));
 assertTrue(isKeyword("interface"));
 assertTrue(isKeyword("let"));
 assertTrue(isKeyword("new"));
 assertTrue(isKeyword("package"));
 assertTrue(isKeyword("private"));
 assertTrue(isKeyword("protected"));
 assertTrue(isKeyword("public"));
 assertTrue(isKeyword("return"));
 assertTrue(isKeyword("static"));
 assertTrue(isKeyword("super"));
 assertTrue(isKeyword("switch"));
 assertTrue(isKeyword("synchronized"));
 assertTrue(isKeyword("this"));
 assertTrue(isKeyword("throw"));
 assertTrue(isKeyword("throws"));
 assertTrue(isKeyword("transient"));
 assertTrue(isKeyword("true"));
 assertTrue(isKeyword("try"));
 assertTrue(isKeyword("typeof"));
 assertTrue(isKeyword("var"));
 assertTrue(isKeyword("void"));
 assertTrue(isKeyword("volatile"));
 assertTrue(isKeyword("while"));
 assertTrue(isKeyword("with"));

 // Testing invalid keywords
 assertFalse(isKeyword("abcd"));
 assertFalse(isKeyword("1234"));
 assertFalse(isKeyword("ABCdef"));
 assertFalse(isKeyword("ABC_def"));
 assertFalse(isKeyword("ABC_def123"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing valid JS identifiers
 assertTrue(isJSIdentifier("identifier1"));
 assertTrue(isJSIdentifier("identifier_1"));
 assertTrue(isJSIdentifier("identifier_123"));
 assertTrue(isJSIdentifier("identifier_123abc"));
 assertTrue(isJSIdentifier("Identifier1"));

 // Testing invalid JS identifiers
 assertFalse(isJSIdentifier("123"));
 assertFalse(isJSIdentifier("identifier 1"));
 assertFalse(isJSIdentifier("identifier-1"));
 assertFalse(isJSIdentifier("identifier-123"));
 assertFalse(isJSIdentifier("identifier_1_2"));
 }
}