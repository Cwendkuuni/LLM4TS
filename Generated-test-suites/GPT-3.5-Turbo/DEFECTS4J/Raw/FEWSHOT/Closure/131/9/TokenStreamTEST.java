
package com.google.javascript.rhino;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("in"));
    assertTrue(TokenStream.isKeyword("do"));
    assertTrue(TokenStream.isKeyword("int"));
    assertTrue(TokenStream.isKeyword("void"));
    assertTrue(TokenStream.isKeyword("with"));
    assertFalse(TokenStream.isKeyword("abc"));
    assertFalse(TokenStream.isKeyword("xyz"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("test"));
    assertTrue(TokenStream.isJSIdentifier("helloWorld"));
    assertTrue(TokenStream.isJSIdentifier("var1"));
    assertTrue(TokenStream.isJSIdentifier("$var"));
    assertFalse(TokenStream.isJSIdentifier("123abc"));
    assertFalse(TokenStream.isJSIdentifier("@name"));
  }
}
