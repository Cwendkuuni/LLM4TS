package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Test positive cases
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
    assertTrue(isKeyword("null"));
    assertTrue(isKeyword("false"));
    assertTrue(isKeyword("true"));
    assertTrue(isKeyword("class"));
    assertTrue(isKeyword("static"));
    assertTrue(isKeyword("public"));
    assertTrue(isKeyword("private"));
    assertTrue(isKeyword("protected"));
    assertTrue(isKeyword("interface"));
    assertTrue(isKeyword("extends"));
    assertTrue(isKeyword("implements"));
    assertTrue(isKeyword("abstract"));
    assertTrue(isKeyword("final"));
    assertTrue(isKeyword("const"));
    assertTrue(isKeyword("package"));

    // Test keyword with different casing
    assertTrue(isKeyword("If"));
    assertTrue(isKeyword("Else"));
    assertTrue(isKeyword("For"));
    assertTrue(isKeyword("While"));
    assertTrue(isKeyword("Switch"));
    assertTrue(isKeyword("Case"));
    assertTrue(isKeyword("Default"));
    assertTrue(isKeyword("Break"));
    assertTrue(isKeyword("Continue"));
    assertTrue(isKeyword("Return"));
    assertTrue(isKeyword("Void"));
    assertTrue(isKeyword("Null"));
    assertTrue(isKeyword("False"));
    assertTrue(isKeyword("True"));
    assertTrue(isKeyword("Class"));
    assertTrue(isKeyword("Static"));
    assertTrue(isKeyword("Public"));
    assertTrue(isKeyword("Private"));
    assertTrue(isKeyword("Protected"));
    assertTrue(isKeyword("Interface"));
    assertTrue(isKeyword("Extends"));
    assertTrue(isKeyword("Implements"));
    assertTrue(isKeyword("Abstract"));
    assertTrue(isKeyword("Final"));
    assertTrue(isKeyword("Const"));
    assertTrue(isKeyword("Package"));

    // Test invalid cases
    assertFalse(isKeyword(""));
    assertFalse(isKeyword("abcd"));
    assertFalse(isKeyword("1234"));
    assertFalse(isKeyword("isKeyword"));
    assertFalse(isKeyword("isKeyword("));
  }

  @Test
  public void testIsJSIdentifier() {
    // Test valid cases
    assertTrue(isJSIdentifier("identifier"));
    assertTrue(isJSIdentifier("Identifier"));
    assertTrue(isJSIdentifier("my_variable"));
    assertTrue(isJSIdentifier("myVariable"));
    assertTrue(isJSIdentifier("_my_variable"));
    assertTrue(isJSIdentifier("my_variable_"));

    // Test invalid cases
    assertFalse(isJSIdentifier(""));
    assertFalse(isJSIdentifier("1234"));
    assertFalse(isJSIdentifier("myVariable="));
    assertFalse(isJSIdentifier("myVariable;"));
    assertFalse(isJSIdentifier("myVariable()"));
  }
}