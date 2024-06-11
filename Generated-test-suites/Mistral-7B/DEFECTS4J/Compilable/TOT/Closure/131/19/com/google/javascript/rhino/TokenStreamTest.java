package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing valid keywords
 assertTrue(isKeyword("false"));
 assertTrue(isKeyword("null"));
 assertTrue(isKeyword("if"));
 assertTrue(isKeyword("else"));
 assertTrue(isKeyword("while"));
 assertTrue(isKeyword("for"));
 assertTrue(isKeyword("break"));
 assertTrue(isKeyword("continue"));
 assertTrue(isKeyword("return"));
 assertTrue(isKeyword("void"));
 assertTrue(isKeyword("int"));
 assertTrue(isKeyword("double"));
 assertTrue(isKeyword("float"));
 assertTrue(isKeyword("boolean"));
 assertTrue(isKeyword("short"));
 assertTrue(isKeyword("long"));
 assertTrue(isKeyword("byte"));
 assertTrue(isKeyword("char"));
 assertTrue(isKeyword("class"));
 assertTrue(isKeyword("export"));
 assertTrue(isKeyword("import"));
 assertTrue(isKeyword("package"));
 assertTrue(isKeyword("public"));
 assertTrue(isKeyword("static"));
 assertTrue(isKeyword("this"));
 assertTrue(isKeyword("super"));
 assertTrue(isKeyword("new"));
 assertTrue(isKeyword("instanceof"));
 assertTrue(isKeyword("typeof"));
 assertTrue(isKeyword("var"));
 assertTrue(isKeyword("let"));
 assertTrue(isKeyword("const"));
 assertTrue(isKeyword("function"));
 assertTrue(isKeyword("export default"));

 // Testing invalid keywords
 assertFalse(isKeyword("invalidKeyword"));
 assertFalse(isKeyword("InvalidKeyword"));
 assertFalse(isKeyword("123"));
 assertFalse(isKeyword("1_2_3"));
 assertFalse(isKeyword("abcd"));
 assertFalse(isKeyword("ABC_DEF"));
 assertFalse(isKeyword("MyVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing valid identifiers
 assertTrue(isJSIdentifier("identifier1"));
 assertTrue(isJSIdentifier("identifier_with_under_score"));
 assertTrue(isJSIdentifier("identifierWithDollarSign"));
 assertTrue(isJSIdentifier("MyVariable"));
 assertTrue(isJSIdentifier("my_variable"));
 assertTrue(isJSIdentifier("myVariable_1"));
 assertTrue(isJSIdentifier("My_Variable_1"));
 assertTrue(isJSIdentifier("identifierWithDigit_1"));

 // Testing invalid identifiers
 assertFalse(isJSIdentifier("identifier1_"));
 assertFalse(isJSIdentifier("identifier1 "));
 assertFalse(isJSIdentifier("identifier1_1"));
 assertFalse(isJSIdentifier("identifier1_1 "));
 assertFalse(isJSIdentifier("identifier1-1"));
 assertFalse(isJSIdentifier("identifier1+1"));
 assertFalse(isJSIdentifier("identifier1#"));
 assertFalse(isJSIdentifier("identifier1%"));
 assertFalse(isJSIdentifier("identifier1^"));
 assertFalse(isJSIdentifier("identifier1&"));
 assertFalse(isJSIdentifier("identifier1|"));
 assertFalse(isJSIdentifier("identifier1~"));
 assertFalse(isJSIdentifier("identifier1!"));
 assertFalse(isJSIdentifier("identifier1@"));
 assertFalse(isJSIdentifier("identifier1$"));
 assertFalse(isJSIdentifier("identifier1?"));
 assertFalse(isJSIdentifier("identifier1[ "));
 assertFalse(isJSIdentifier("identifier1]"));
 assertFalse(isJSIdentifier("identifier1{ "));
 assertFalse(isJSIdentifier("identifier1}"));
 assertFalse(isJSIdentifier("identifier1("));
 assertFalse(isJSIdentifier("identifier1)"));
 assertFalse(isJSIdentifier("identifier1;"));
 }
}