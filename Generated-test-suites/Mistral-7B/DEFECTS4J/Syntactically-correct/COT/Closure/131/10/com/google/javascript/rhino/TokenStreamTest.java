package com.google.javascript.rhino.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.TokenStream;

import org.junit.Test;

/**
 * Test class for TokenStream methods.
 */
public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Basic testing
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("while"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("switch"));
    assertTrue(TokenStream.isKeyword("case"));
    assertTrue(TokenStream.isKeyword("default"));
    assertTrue(TokenStream.isKeyword("break"));
    assertTrue(TokenStream.isKeyword("continue"));
    assertTrue(TokenStream.isKeyword("return"));
    assertTrue(TokenStream.isKeyword("var"));
    assertTrue(TokenStream.isKeyword("let"));
    assertTrue(TokenStream.isKeyword("const"));
    assertTrue(TokenStream.isKeyword("function"));
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("export"));
    assertTrue(TokenStream.isKeyword("import"));
    assertTrue(TokenStream.isKeyword("this"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("true"));
    assertTrue(TokenStream.isKeyword("false"));
    assertTrue(TokenStream.isKeyword("null"));
    assertTrue(TokenStream.isKeyword("void"));
    assertTrue(TokenStream.isKeyword("with"));
    assertTrue(TokenStream.isKeyword("in"));
    assertTrue(TokenStream.isKeyword("delete"));
    assertTrue(TokenStream.isKeyword("try"));
    assertTrue(TokenStream.isKeyword("catch"));
    assertTrue(TokenStream.isKeyword("throw"));
    assertTrue(TokenStream.isKeyword("finally"));
    assertTrue(TokenStream.isKeyword("breakpoint")); // Rhino specific
    assertTrue(TokenStream.isKeyword("interface"));
    assertTrue(TokenStream.isKeyword("package"));
    assertTrue(TokenStream.isKeyword("public"));
    assertTrue(TokenStream.isKeyword("static"));
    assertTrue(TokenStream.isKeyword("private"));
    assertTrue(TokenStream.isKeyword("protected"));
    assertTrue(TokenStream.isKeyword("abstract"));
    assertTrue(TokenStream.isKeyword("transient"));
    assertTrue(TokenStream.isKeyword("synchronized"));
    assertTrue(TokenStream.isKeyword("yield")); // ECMAScript 6
    assertTrue(TokenStream.isKeyword("export default")); // ECMAScript 6
    assertTrue(TokenStream.isKeyword("import default")); // ECMAScript 6

    // Edge cases and exception handling
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword(" "));
    assertFalse(TokenStream.isKeyword("1"));
    assertFalse(TokenStream.isKeyword("123"));
    assertFalse(TokenStream.isKeyword("isKeyword"));
    assertFalse(TokenStream.isKeyword("ISKEYWORD"));
    assertFalse(TokenStream.isKeyword("IsKeyword"));
    assertFalse(TokenStream.isKeyword("ifElse"));
    assertFalse(TokenStream.isKeyword("IFELSE"));
    assertFalse(TokenStream.isKeyword("IfElse"));
    assertFalse(TokenStream.isKeyword("if_else"));
    assertFalse(TokenStream.isKeyword("IF_ELSE"));
    assertFalse(TokenStream.isKeyword("If_Else"));
  }

  @Test
  public void testIsJSIdentifier() {
    // Basic testing
    assertTrue(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier(""));
    assertTrue(TokenStream.isJSIdentifier("a"));
    assertTrue(TokenStream.isJSIdentifier("ab"));
    assertTrue(TokenStream.isJSIdentifier("ab1"));
    assertTrue(TokenStream.isJSIdentifier("_name"));
    assertTrue(TokenStream.isJSIdentifier("name_"));
    assertTrue(TokenStream.isJSIdentifier("name1"));
    assertTrue(TokenStream.isJSIdentifier("name123"));
    assertTrue(TokenStream.isJSIdentifier("name_123"));
    assertTrue(TokenStream.isJSIdentifier("name_123_"));
    assertTrue(TokenStream.isJSIdentifier("_name123"));
    assertTrue(TokenStream.isJSIdentifier("name123_"));
    assertTrue(TokenStream.isJSIdentifier("name1_23"));
    assertTrue(TokenStream.isJSIdentifier("name1_23_"));

    // Edge cases and exception handling
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier(" "));
    assertFalse(TokenStream.isJSIdentifier("1"));
    assertFalse(TokenStream.isJSIdentifier("123"));
    assertFalse(TokenStream.isJSIdentifier("isIdentifier"));
    assertFalse(TokenStream.isJSIdentifier("ISIDENTIFIER"));
    assertFalse(TokenStream.isJSIdentifier("IsIdentifier"));
    assertFalse(TokenStream.isJSIdentifier("isIdentifier1"));
    assertFalse(TokenStream.isJSIdentifier("ISIDENTIFIER1"));
    assertFalse(TokenStream.isJSIdentifier("IsIdentifier1"));
    assertFalse(TokenStream.isJSIdentifier("isIdentifier_"));
    assertFalse(TokenStream.isJSIdentifier("ISIDENTIFIER_"));
    assertFalse(TokenStream.isJSIdentifier("IsIdentifier_"));
    assertFalse(TokenStream.isJSIdentifier("isIdentifier123"));
    assertFalse(TokenStream.isJSIdentifier("ISIDENTIFIER123"));
    assertFalse(TokenStream.isJSIdentifier("IsIdentifier123"));
  }
}