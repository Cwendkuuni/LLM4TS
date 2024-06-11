package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("for"));
 assertEquals(true, TokenStream.isKeyword("switch"));
 assertEquals(true, TokenStream.isKeyword("case"));
 assertEquals(true, TokenStream.isKeyword("default"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("continue"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("void"));
 assertEquals(true, TokenStream.isKeyword("true"));
 assertEquals(true, TokenStream.isKeyword("false"));
 assertEquals(true, TokenStream.isKeyword("null"));
 assertEquals(true, TokenStream.isKeyword("this"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("static"));
 assertEquals(true, TokenStream.isKeyword("public"));
 assertEquals(true, TokenStream.isKeyword("protected"));
 assertEquals(true, TokenStream.isKeyword("private"));
 assertEquals(true, TokenStream.isKeyword("export"));
 assertEquals(true, TokenStream.isKeyword("import"));
 assertEquals(true, TokenStream.isKeyword("interface"));
 assertEquals(true, TokenStream.isKeyword("implements"));
 assertEquals(true, TokenStream.isKeyword("transient"));
 assertEquals(true, TokenStream.isKeyword("synchronized"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("myVariable"));
 assertFalse(TokenStream.isKeyword("my_variable"));
 assertFalse(TokenStream.isKeyword("my_variable_1"));
 assertFalse(TokenStream.isKeyword("myVariable1"));
 assertFalse(TokenStream.isKeyword("MyVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertEquals(true, TokenStream.isJSIdentifier("myVariable"));
 assertEquals(true, TokenStream.isJSIdentifier("my_variable"));
 assertEquals(true, TokenStream.isJSIdentifier("my_variable_1"));
 assertEquals(true, TokenStream.isJSIdentifier("MyVariable"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier("abc"));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("myVariable123"));
 assertFalse(TokenStream.isJSIdentifier("my_variable123"));
 assertFalse(TokenStream.isJSIdentifier("MyVariable123"));
 }
}