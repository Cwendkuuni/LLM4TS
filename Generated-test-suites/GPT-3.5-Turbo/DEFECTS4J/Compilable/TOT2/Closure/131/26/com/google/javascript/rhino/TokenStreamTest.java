package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword_basic() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertFalse(TokenStream.isKeyword("hello"));
  }

  @Test
  public void testIsJSIdentifier_basic() {
    assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
    assertFalse(TokenStream.isJSIdentifier("1invalidIdentifier"));
  }

  @Test
  public void testIsKeyword_edgeCases() {
    assertTrue(TokenStream.isKeyword("return"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("switch"));
    assertFalse(TokenStream.isKeyword("notakeyword"));
  }

  @Test
  public void testIsJSIdentifier_edgeCases() {
    assertTrue(TokenStream.isJSIdentifier("validIdentifier123"));
    assertFalse(TokenStream.isJSIdentifier(" withs p a c e s "));
  }

  @Test
  public void testIsJSIdentifier_exceptionHandling() {
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier("!@#$%"));
  }
}