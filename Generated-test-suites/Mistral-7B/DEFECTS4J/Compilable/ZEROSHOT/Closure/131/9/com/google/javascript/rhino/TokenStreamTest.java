package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing true cases
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("for"));
 assertEquals(true, TokenStream.isKeyword("try"));
 assertEquals(true, TokenStream.isKeyword("catch"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("catch"));
 assertEquals(true, TokenStream.isKeyword("function"));
 assertEquals(true, TokenStream.isKeyword("case"));
 assertEquals(true, TokenStream.isKeyword("default"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("switch"));
 assertEquals(true, TokenStream.isKeyword("export"));
 assertEquals(true, TokenStream.isKeyword("import"));
 assertEquals(true, TokenStream.isKeyword("this"));
 assertEquals(true, TokenStream.isKeyword("new"));
 assertEquals(true, TokenStream.isKeyword("delete"));
 assertEquals(true, TokenStream.isKeyword("instanceof"));
 assertEquals(true, TokenStream.isKeyword("typeof"));
 assertEquals(true, TokenStream.isKeyword("void"));
 assertEquals(true, TokenStream.isKeyword("null"));
 assertEquals(true, TokenStream.isKeyword("true"));
 assertEquals(true, TokenStream.isKeyword("false"));

 // Testing false cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("MyClass"));
 assertFalse(TokenStream.isKeyword("myFunction"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing true cases
 assertEquals(true, TokenStream.isJSIdentifier("identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("myIdentifier_123"));
 assertEquals(true, TokenStream.isJSIdentifier("myIdentifierWith$ underscore_"));
 assertEquals(true, TokenStream.isJSIdentifier("myIdentifierWith_underscore"));
 assertEquals(true, TokenStream.isJSIdentifier("myIdentifierWith$"));
 assertEquals(true, TokenStream.isJSIdentifier("myIdentifierWith_"));

 // Testing false cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("abc"));
 assertFalse(TokenStream.isJSIdentifier("myIdentifierWith!@#"));
 }
}