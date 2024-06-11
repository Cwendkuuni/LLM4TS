package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Test true cases
    assertTrue(isKeyword("if"));
    assertTrue(isKeyword("else"));
    assertTrue(isKeyword("for"));
    assertTrue(isKeyword("while"));
    assertTrue(isKeyword("switch"));
    assertTrue(isKeyword("case"));
    assertTrue(isKeyword("default"));
    assertTrue(isKeyword("break"));
    assertTrue(isKeyword("continue"));
    assertTrue(isKeyword("return"));
    assertTrue(isKeyword("void"));
    assertTrue(isKeyword("boolean"));
    assertTrue(isKeyword("int"));
    assertTrue(isKeyword("float"));
    assertTrue(isKeyword("short"));
    assertTrue(isKeyword("long"));
    assertTrue(isKeyword("byte"));
    assertTrue(isKeyword("char"));
    assertTrue(isKeyword("double"));
    assertTrue(isKeyword("static"));
    assertTrue(isKeyword("public"));
    assertTrue(isKeyword("private"));
    assertTrue(isKeyword("protected"));
    assertTrue(isKeyword("abstract"));
    assertTrue(isKeyword("interface"));
    assertTrue(isKeyword("implements"));
    assertTrue(isKeyword("class"));
    assertTrue(isKeyword("package"));
    assertTrue(isKeyword("extends"));
    assertTrue(isKeyword("transient"));
    assertTrue(isKeyword("native"));
    assertTrue(isKeyword("const"));
    assertTrue(isKeyword("final"));
    assertTrue(isKeyword("goto"));
    assertTrue(isKeyword("throw"));
    assertTrue(isKeyword("try"));
    assertTrue(isKeyword("catch"));
    assertTrue(isKeyword("throws"));
    assertTrue(isKeyword("import"));
    assertTrue(isKeyword("export"));
    assertTrue(isKeyword("this"));
    assertTrue(isKeyword("new"));

    // Test false cases
    assertFalse(isKeyword("Test"));
    assertFalse(isKeyword("test"));
    assertFalse(isKeyword("var"));
    assertFalse(isKeyword("let"));
    assertFalse(isKeyword("const"));
    assertFalse(isKeyword("function"));
    assertFalse(isKeyword("class"));
    assertFalse(isKeyword("instanceof"));
    assertFalse(isKeyword("return"));
  }

  @Test
  public void testIsJSIdentifier() {
    // Test true cases
    assertTrue(isJSIdentifier("foo"));
    assertTrue(isJSIdentifier("bar_baz"));
    assertTrue(isJSIdentifier("my_var"));
    assertTrue(isJSIdentifier("myVar"));
    assertTrue(isJSIdentifier("my_long_variable_name"));
    assertTrue(isJSIdentifier("invalid_identifier_123"));

    // Test false cases
    assertFalse(isJSIdentifier("123"));
    assertFalse(isJSIdentifier("foo bar"));
    assertFalse(isJSIdentifier("foo-bar"));
    assertFalse(isJSIdentifier("foo_bar123"));
    assertFalse(isJSIdentifier("my var"));
    assertFalse(isJSIdentifier("my Var"));
    assertFalse(isJSIdentifier("my_var="));
    assertFalse(isJSIdentifier("my_var_="));
  }
}