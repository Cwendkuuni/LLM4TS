package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("long"));
    assertTrue(TokenStream.isKeyword("void"));
    assertTrue(TokenStream.isKeyword("null"));
    assertTrue(TokenStream.isKeyword("boolean"));
    
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword("invalid"));
    assertFalse(TokenStream.isKeyword("length"));
  }

  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("variable1"));
    assertTrue(TokenStream.isJSIdentifier("variable_2"));
    assertTrue(TokenStream.isJSIdentifier("variable$3"));
    
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier("1variable"));
    assertFalse(TokenStream.isJSIdentifier("va&riable"));
    assertFalse(TokenStream.isJSIdentifier("variable!4"));
  }

}