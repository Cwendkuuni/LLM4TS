package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_knownKeywords() {
 // Test known keywords
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("else if"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("export default"));
 assertTrue(TokenStream.isKeyword("import default"));
 assertTrue(TokenStream.isKeyword("as"));
 assertTrue(TokenStream.isKeyword("await"));
 assertTrue(TokenStream.isKeyword("yield"));
 assertTrue(TokenStream.isKeyword("async"));

 // Test case sensitivity
 assertTrue(TokenStream.isKeyword("If"));
 assertFalse(TokenStream.isKeyword("if "));
 assertFalse(TokenStream.isKeyword("iF"));
 }

 @Test
 public void testIsKeyword_unknownString() {
 // Test unknown strings
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("foo"));
 assertFalse(TokenStream.isKeyword("fooBar"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("1.2.3"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Test valid identifiers
 assertTrue(TokenStream.isJSIdentifier("foo"));
 assertTrue(TokenStream.isJSIdentifier("fooBar"));
 assertTrue(TokenStream.isJSIdentifier("foo_bar"));
 assertTrue(TokenStream.isJSIdentifier("foo_123"));
 assertTrue(TokenStream.isJSIdentifier("foo_123_bar"));
 assertTrue(TokenStream.isJSIdentifier("foo_123_bar_123"));

 // Test invalid identifiers
 assertFalse(TokenStream.isJSIdentifier("foo "));
 assertFalse(TokenStream.isJSIdentifier("foo\tbar"));
 assertFalse(TokenStream.isJSIdentifier("foo\rbar"));
 assertFalse(TokenStream.isJSIdentifier("foo\nbar"));
 assertFalse(TokenStream.isJSIdentifier("foo123"));
 assertFalse(TokenStream.isJSIdentifier("foo-bar"));
 assertFalse(TokenStream.isJSIdentifier("foo@bar"));
 assertFalse(TokenStream.isJSIdentifier("foo_123_bar-123"));
 }
}