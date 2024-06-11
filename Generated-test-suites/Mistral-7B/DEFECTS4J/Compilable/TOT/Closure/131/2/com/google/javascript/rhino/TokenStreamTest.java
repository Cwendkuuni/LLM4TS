package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing for true cases
 assertTrue(isKeyword("if"));
 assertTrue(isKeyword("else"));
 assertTrue(isKeyword("while"));
 assertTrue(isKeyword("for"));
 assertTrue(isKeyword("switch"));
 assertTrue(isKeyword("case"));
 assertTrue(isKeyword("default"));
 assertTrue(isKeyword("try"));
 assertTrue(isKeyword("catch"));
 assertTrue(isKeyword("throw"));
 assertTrue(isKeyword("return"));
 assertTrue(isKeyword("break"));
 assertTrue(isKeyword("continue"));
 assertTrue(isKeyword("boolean"));
 assertTrue(isKeyword("byte"));
 assertTrue(isKeyword("short"));
 assertTrue(isKeyword("int"));
 assertTrue(isKeyword("long"));
 assertTrue(isKeyword("float"));
 assertTrue(isKeyword("double"));
 assertTrue(isKeyword("char"));
 assertTrue(isKeyword("void"));
 assertTrue(isKeyword("null"));
 assertTrue(isKeyword("class"));
 assertTrue(isKeyword("interface"));
 assertTrue(isKeyword("function"));
 assertTrue(isKeyword("export"));

 // Testing for false cases (non-keywords)
 assertFalse(isKeyword("test"));
 assertFalse(isKeyword("myFunction"));
 assertFalse(isKeyword("myClass"));
 assertFalse(isKeyword("myVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing for true cases
 assertTrue(isJSIdentifier("identifier1"));
 assertTrue(isJSIdentifier("identifier2_with_underscore"));
 assertTrue(isJSIdentifier("identifier3WithCamelCase"));
 assertTrue(isJSIdentifier("identifier4WithDigits_123"));

 // Testing for false cases (invalid identifiers)
 assertFalse(isJSIdentifier("identifier1_")); // missing first character
 assertFalse(isJSIdentifier("identifier1.")); // invalid character '.' at position 1
 assertFalse(isJSIdentifier("identifier1_0")); // invalid character '0' at position 1
 assertFalse(isJSIdentifier("identifier1_abc")); // invalid characters 'a', 'b', 'c' at position 2, 3, 4
 }
}