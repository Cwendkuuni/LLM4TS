package com.google.javascript.rhino.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.TokenStream;

/**
 * Tests for the TokenStream class.
 */
public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Basic test cases
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("while"));
    assertTrue(TokenStream.isKeyword("var"));
    assertTrue(TokenStream.isKeyword("let"));
    assertTrue(TokenStream.isKeyword("const"));
    assertTrue(TokenStream.isKeyword("function"));
    assertTrue(TokenStream.isKeyword("return"));
    assertTrue(TokenStream.isKeyword("boolean"));
    assertTrue(TokenStream.isKeyword("null"));
    assertTrue(TokenStream.isKeyword("console"));

    // Edge cases and exception handling
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword(" "));
    assertFalse(TokenStream.isKeyword("1"));
    assertFalse(TokenStream.isKeyword("_"));
    assertFalse(TokenStream.isKeyword("abc"));
    assertFalse(TokenStream.isKeyword("ABC"));
    assertFalse(TokenStream.isKeyword("ABCDEF"));
    assertFalse(TokenStream.isKeyword("try"));
    assertFalse(TokenStream.isKeyword("catch"));
    assertFalse(TokenStream.isKeyword("throw"));
    assertFalse(TokenStream.isKeyword("class"));
    assertFalse(TokenStream.isKeyword("import"));
    assertFalse(TokenStream.isKeyword("export"));
    assertFalse(TokenStream.isKeyword("native"));
    assertFalse(TokenStream.isKeyword("package"));
    assertFalse(TokenStream.isKeyword("abstract"));
    assertFalse(TokenStream.isKeyword("continue"));
    assertFalse(TokenStream.isKeyword("debugger"));
    assertFalse(TokenStream.isKeyword("interface"));
    assertFalse(TokenStream.isKeyword("protected"));
    assertFalse(TokenStream.isKeyword("transient"));
    assertFalse(TokenStream.isKeyword("synchronized"));
    assertFalse(TokenStream.isKeyword("new"));
  }

  @Test
  public void testIsJSIdentifier() {
    // Basic test cases
    assertTrue(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier("a"));
    assertTrue(TokenStream.isJSIdentifier("_"));
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertTrue(TokenStream.isJSIdentifier("ABC"));
    assertTrue(TokenStream.isJSIdentifier("ABCDEF"));
    assertTrue(TokenStream.isJSIdentifier("123"));
    assertTrue(TokenStream.isJSIdentifier("_123"));
    assertTrue(TokenStream.isJSIdentifier("abc123"));

    // Edge cases and exception handling
    assertFalse(TokenStream.isJSIdentifier(" "));
    assertFalse(TokenStream.isJSIdentifier("1"));
    assertFalse(TokenStream.isJSIdentifier("if"));
    assertFalse(TokenStream.isJSIdentifier("for"));
    assertFalse(TokenStream.isJSIdentifier("while"));
    assertFalse(TokenStream.isJSIdentifier("var"));
    assertFalse(TokenStream.isJSIdentifier("let"));
    assertFalse(TokenStream.isJSIdentifier("const"));
    assertFalse(TokenStream.isJSIdentifier("function"));
    assertFalse(TokenStream.isJSIdentifier("return"));
    assertFalse(TokenStream.isJSIdentifier("boolean"));
    assertFalse(TokenStream.isJSIdentifier("null"));
    assertFalse(TokenStream.isJSIdentifier("console"));
    assertFalse(TokenStream.isJSIdentifier("try"));
    assertFalse(TokenStream.isJSIdentifier("catch"));
    assertFalse(TokenStream.isJSIdentifier("throw"));
    assertFalse(TokenStream.isJSIdentifier("class"));
    assertFalse(TokenStream.isJSIdentifier("import"));
    assertFalse(TokenStream.isJSIdentifier("export"));
    assertFalse(TokenStream.isJSIdentifier("native"));
    assertFalse(TokenStream.isJSIdentifier("package"));
    assertFalse(TokenStream.isJSIdentifier("abstract"));
    assertFalse(TokenStream.isJSIdentifier("continue"));
    assertFalse(TokenStream.isJSIdentifier("debugger"));
    assertFalse(TokenStream.isJSIdentifier("interface"));
    assertFalse(TokenStream.isJSIdentifier("protected"));
    assertFalse(TokenStream.isJSIdentifier("transient"));
    assertFalse(TokenStream.isJSIdentifier("synchronized"));
    assertFalse(TokenStream.isJSIdentifier("new"));
  }
}