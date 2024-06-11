package com.google.javascript.rhino.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.TokenStream;

import org.junit.Test;

/**
 * Test cases for the TokenStream class.
 */
public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Basic test cases for isKeyword method
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("else if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("function"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("return"));
    assertTrue(TokenStream.isKeyword("try"));
    assertTrue(TokenStream.isKeyword("var"));
    assertTrue(TokenStream.isKeyword("while"));
    assertTrue(TokenStream.isKeyword("with"));
    assertTrue(TokenStream.isKeyword("this"));
    assertTrue(TokenStream.isKeyword("break"));
    assertTrue(TokenStream.isKeyword("continue"));
    assertTrue(TokenStream.isKeyword("switch"));
    assertTrue(TokenStream.isKeyword("throw"));
    assertTrue(TokenStream.isKeyword("case"));
    assertTrue(TokenStream.isKeyword("default"));
    assertTrue(TokenStream.isKeyword("export"));
    assertTrue(TokenStream.isKeyword("import"));
    assertTrue(TokenStream.isKeyword("package"));
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("interface"));
    assertTrue(TokenStream.isKeyword("extends"));
    assertTrue(TokenStream.isKeyword("implements"));
    assertTrue(TokenStream.isKeyword("static"));
    assertTrue(TokenStream.isKeyword("public"));
    assertTrue(TokenStream.isKeyword("private"));
    assertTrue(TokenStream.isKeyword("protected"));
    assertTrue(TokenStream.isKeyword("abstract"));
    assertTrue(TokenStream.isKeyword("final"));
    assertTrue(TokenStream.isKeyword("const"));
    assertTrue(TokenStream.isKeyword("native"));
    assertTrue(TokenStream.isKeyword("transient"));
    assertTrue(TokenStream.isKeyword("yield"));
    assertTrue(TokenStream.isKeyword("import"));
    assertTrue(TokenStream.isKeyword("export"));
    assertTrue(TokenStream.isKeyword("delete"));

    // Edge cases and exception handling test cases for isKeyword method
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword(null));
    assertFalse(TokenStream.isKeyword(" "));
    assertFalse(TokenStream.isKeyword("if("));
    assertFalse(TokenStream.isKeyword("if)"));
    assertFalse(TokenStream.isKeyword("if("));
    assertFalse(TokenStream.isKeyword("if("));
    assertFalse(TokenStream.isKeyword("if_")); // invalid identifier
    assertFalse(TokenStream.isKeyword("if_keyword")); // keyword with underscore
    assertFalse(TokenStream.isKeyword("ifKeyword")); // keyword with capitalized first letter
  }

  @Test
  public void testIsJSIdentifier() {
    // Basic test cases for isJSIdentifier method
    assertTrue(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier("_"));
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertTrue(TokenStream.isJSIdentifier("Abc"));
    assertTrue(TokenStream.isJSIdentifier("123"));
    assertTrue(TokenStream.isJSIdentifier("123_"));
    assertTrue(TokenStream.isJSIdentifier("123_Abc"));
    assertTrue(TokenStream.isJSIdentifier("abc123"));
    assertTrue(TokenStream.isJSIdentifier("abc123_"));
    assertTrue(TokenStream.isJSIdentifier("abc_123"));
    assertTrue(TokenStream.isJSIdentifier("Abc123"));
    assertTrue(TokenStream.isJSIdentifier("Abc123_"));
    assertTrue(TokenStream.isJSIdentifier("Abc_123"));

    // Edge cases and exception handling test cases for isJSIdentifier method
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier(null));
    assertFalse(TokenStream.isJSIdentifier(" "));
    assertFalse(TokenStream.isJSIdentifier("if")); // keyword
    assertFalse(TokenStream.isJSIdentifier("if(")); // left parenthesis
    assertFalse(TokenStream.isJSIdentifier("if)")); // right parenthesis
    assertFalse(TokenStream.isJSIdentifier("if_")); // invalid identifier
    assertFalse(TokenStream.isJSIdentifier("ifKeyword")); // keyword with underscore
    assertFalse(TokenStream.isJSIdentifier("ifKeyword_")); // keyword with underscore and following character
  }
}