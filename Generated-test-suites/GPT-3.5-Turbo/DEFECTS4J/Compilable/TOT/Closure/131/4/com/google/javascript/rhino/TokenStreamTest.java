package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    // Typical use case: check for a valid keyword "class"
    assertTrue(TokenStream.isKeyword("class"));
    
    // Edge case: check for the shortest valid keyword "if"
    assertTrue(TokenStream.isKeyword("if"));

    // Error scenario: check for an invalid keyword "func"
    assertFalse(TokenStream.isKeyword("func"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    // Typical use case: check for a valid JavaScript identifier "variable"
    assertTrue(TokenStream.isJSIdentifier("variable"));
    
    // Edge case: check for a valid single character identifier "a"
    assertTrue(TokenStream.isJSIdentifier("a"));

    // Error scenario: check for an invalid identifier starting with a number "1var"
    assertFalse(TokenStream.isJSIdentifier("1var"));
  }

  // Additional test cases can be added for other methods in TokenStream class
}