package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertFalse(TokenStream.isKeyword("abc"));
    assertFalse(TokenStream.isKeyword("123"));
    assertTrue(TokenStream.isKeyword("byte"));
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("null"));
    assertTrue(TokenStream.isKeyword("super"));
    assertTrue(TokenStream.isKeyword("true"));
    assertFalse(TokenStream.isKeyword("fal"));
    assertFalse(TokenStream.isKeyword("abc"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertFalse(TokenStream.isJSIdentifier("123"));
    assertFalse(TokenStream.isJSIdentifier("_abc"));
    assertFalse(TokenStream.isJSIdentifier("abc!"));
    assertTrue(TokenStream.isJSIdentifier("testVar"));
    assertTrue(TokenStream.isJSIdentifier("camelCase"));
    assertTrue(TokenStream.isJSIdentifier("$variable"));
    assertFalse(TokenStream.isJSIdentifier("if"));
    assertFalse(TokenStream.isJSIdentifier("while"));
  }
}