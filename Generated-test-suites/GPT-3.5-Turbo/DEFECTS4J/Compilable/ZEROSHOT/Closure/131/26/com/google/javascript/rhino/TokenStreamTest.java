package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("while"));
    assertFalse(TokenStream.isKeyword("foo"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("foo"));
    assertTrue(TokenStream.isJSIdentifier("bar"));
    assertTrue(TokenStream.isJSIdentifier("baz"));
    assertFalse(TokenStream.isJSIdentifier("123"));
  }
}