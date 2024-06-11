package com.google.javascript.rhino;
import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Typical use case
    assertFalse(TokenStream.isKeyword("hello"));
    
    // Starting and ending edge cases
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword("c"));
    assertFalse(TokenStream.isKeyword("const"));
    assertFalse(TokenStream.isKeyword("final"));
    
    // Error scenario
    assertFalse(TokenStream.isKeyword(null));
  }

  @Test
  public void testIsJSIdentifier() {
    // Typical use case
    assertTrue(TokenStream.isJSIdentifier("test"));
    
    // Starting and ending edge cases
    assertFalse(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier("a"));
    assertTrue(TokenStream.isJSIdentifier("alphabet12"));
    assertTrue(TokenStream.isJSIdentifier("number5number"));
    assertTrue(TokenStream.isJSIdentifier("a_b_c"));
    
    // Error scenario
    assertFalse(TokenStream.isJSIdentifier(null));
    assertFalse(TokenStream.isJSIdentifier("1num"));
    assertFalse(TokenStream.isJSIdentifier("test@me"));
  }

}