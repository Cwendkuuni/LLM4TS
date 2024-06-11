package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Typical use cases
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("float"));
    assertTrue(TokenStream.isKeyword("extends"));

    // Edge cases
    assertTrue(TokenStream.isKeyword("void"));
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("synchronized"));

    // Error scenarios
    assertFalse(TokenStream.isKeyword("if1"));
    assertFalse(TokenStream.isKeyword("foreach"));
    assertFalse(TokenStream.isKeyword("1while"));

  }

  @Test
  public void testIsJSIdentifier() {
    // Typical use cases
    assertTrue(TokenStream.isJSIdentifier("myVariable"));
    assertTrue(TokenStream.isJSIdentifier("_privateVar"));
    assertTrue(TokenStream.isJSIdentifier("$friendlyVar"));

    // Edge cases
    assertTrue(TokenStream.isJSIdentifier("i"));
    assertTrue(TokenStream.isJSIdentifier("$"));
    assertTrue(TokenStream.isJSIdentifier("_"));

    // Error scenarios
    assertFalse(TokenStream.isJSIdentifier("my Variable"));
    assertFalse(TokenStream.isJSIdentifier("123var"));
    assertFalse(TokenStream.isJSIdentifier("1$variable"));
  }

}