package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("new"));
    assertFalse(TokenStream.isKeyword("banana"));
    assertFalse(TokenStream.isKeyword("apple"));
    assertFalse(TokenStream.isKeyword("kiwi"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertTrue(TokenStream.isJSIdentifier("ABC"));
    assertTrue(TokenStream.isJSIdentifier("_abc123"));
    assertFalse(TokenStream.isJSIdentifier("123abc"));
    assertFalse(TokenStream.isJSIdentifier("abc$"));
    assertFalse(TokenStream.isJSIdentifier("abc!"));
  }
}