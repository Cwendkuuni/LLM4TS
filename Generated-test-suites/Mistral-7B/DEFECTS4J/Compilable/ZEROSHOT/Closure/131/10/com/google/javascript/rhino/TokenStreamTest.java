package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
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
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("byte"));
 assertTrue(TokenStream.isKeyword("short"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("long"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("char"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("export"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("myVariable"));
 assertFalse(TokenStream.isKeyword("myFunction"));
 assertFalse(TokenStream.isKeyword("MyClass"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier("myVariable"));
 assertTrue(TokenStream.isJSIdentifier("myFunction"));
 assertTrue(TokenStream.isJSIdentifier("MyClass"));
 assertTrue(TokenStream.isJSIdentifier("_myVariable"));
 assertTrue(TokenStream.isJSIdentifier("my_variable"));
 assertTrue(TokenStream.isJSIdentifier("myVariable123"));
 assertTrue(TokenStream.isJSIdentifier("my_variable_123"));
 assertTrue(TokenStream.isJSIdentifier("myVariable_123"));
 assertTrue(TokenStream.isJSIdentifier("my_variable123"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier("abc"));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("myVariable123ABC"));
 assertFalse(TokenStream.isJSIdentifier("myVariable-123"));
 assertFalse(TokenStream.isJSIdentifier("myVariable123_"));
 assertFalse(TokenStream.isJSIdentifier("myVariable123__"));
 }
}