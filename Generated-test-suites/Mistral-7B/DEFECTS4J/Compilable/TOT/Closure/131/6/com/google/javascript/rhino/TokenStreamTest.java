package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

/**
 * Test cases for the TokenStream class.
 */
public class TokenStreamTest {

  /**
   * Test isKeyword method for valid JavaScript keywords.
   */
  @Test
  public void testIsKeywordValid() {
    String[] keywords = {
      "abstract", "arguments", "boolean", "break", "byte", "case",
      "catch", "char", "class", "const", "continue", "debugger",
      "default", "delete", "do", "double", "else", "export",
      "extends", "false", "finally", "float", "for", "function",
      "goto", "if", "implements", "import", "in", "instanceof",
      "int", "interface", "let", "long", "native", "new",
      "null", "package", "private", "protected", "public",
      "return", "short", "static", "super", "switch", "synchronized",
      "this", "throw", "throws", "transient", "true", "try",
      "typeof", "var", "void", "volatile", "while", "with",
      "yield"
    };

    for (String keyword : keywords) {
      assertTrue(isKeyword(keyword));
    }
  }

  /**
   * Test isKeyword method for invalid strings.
   */
  @Test
  public void testIsKeywordInvalid() {
    String[] invalidStrings = {
      "test", "AbCdEfGhIjKlMnOpQrStUvWxYz", "1234567890", "myVariable",
      "my_variable", "my_very_long_variable", "myVeryLongVariable",
      "my_very_long_variable_with_numbers", "my-very-long-variable_with_numbers",
      "myVariable_with_numbers", "myVariableWithNumbers",
      "my_very_long_variable_with_numbers_and_underscores",
      "myVariableWithNumbersAndUnderscores"
    };

    for (String invalidString : invalidStrings) {
      assertFalse(isKeyword(invalidString));
    }
  }

  /**
   * Test isJSIdentifier method for valid JavaScript identifiers.
   */
  @Test
  public void testIsJSIdentifierValid() {
    String[] identifiers = {
      "myVariable", "myVariable_1", "my_long_variable",
      "myLongVariable", "my_variable_with_numbers",
      "myVariableWithNumbers", "my_very_long_variable",
      "myVariable_with_underscores_and_numbers"
    };

    for (String identifier : identifiers) {
      assertTrue(isJSIdentifier(identifier));
    }
  }

  /**
   * Test isJSIdentifier method for invalid strings.
   */
  @Test
  public void testIsJSIdentifierInvalid() {
    String[] invalidStrings = {
      "123", "my_variable_with_spaces", "my-variable_with_hyphen",
      "myVariableWithNumbers_and_hyphen", "myLongVariableWithNumbers_And_Hyphen"
    };

    for (String invalidString : invalidStrings) {
      assertFalse(isJSIdentifier(invalidString));
    }
  }
}