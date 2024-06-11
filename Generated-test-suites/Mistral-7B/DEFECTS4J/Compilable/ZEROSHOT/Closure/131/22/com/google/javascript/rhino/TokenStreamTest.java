package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("boolean"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("myVariable"));
 assertFalse(TokenStream.isKeyword("aBcDeF"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("identifier"));
 assertTrue(TokenStream.isJSIdentifier("_identifier"));
 assertTrue(TokenStream.isJSIdentifier("identifier_"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("myVariable123"));
 assertFalse(TokenStream.isJSIdentifier("abcdABCdeF"));
 assertFalse(TokenStream.isJSIdentifier("identifier123"));
 }
}