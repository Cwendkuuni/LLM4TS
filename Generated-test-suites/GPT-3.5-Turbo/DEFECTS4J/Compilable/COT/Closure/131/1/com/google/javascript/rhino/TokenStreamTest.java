package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
  @Test
  public void testIsKeyword() {
    // Testing a keyword as a valid name
    assertTrue(TokenStream.isKeyword("if"));

    // Testing a non-keyword as a valid name
    assertFalse(TokenStream.isKeyword("test"));

    // Testing an empty name as an invalid input
    assertFalse(TokenStream.isKeyword(""));
  }

  @Test
  public void testIsJSIdentifier() {
    // Testing a valid JavaScript identifier
    assertTrue(TokenStream.isJSIdentifier("test"));

    // Testing a JavaScript identifier starting with a number
    assertFalse(TokenStream.isJSIdentifier("123test"));

    // Testing an empty string as an invalid input
    assertFalse(TokenStream.isJSIdentifier(""));
  }
}