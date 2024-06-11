package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("do"));
    assertFalse(TokenStream.isKeyword("abc"));
  }

  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertTrue(TokenStream.isJSIdentifier("_var"));
    assertFalse(TokenStream.isJSIdentifier("123abc"));
  }

}