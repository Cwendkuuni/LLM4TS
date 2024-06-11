package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Test positive cases
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("as"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("byte"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("char"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("debugger"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("do"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("final"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("goto"));
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("long"));
 assertTrue(TokenStream.isKeyword("native"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("short"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("synchronized"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("volatile"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("with"));

 // Test negative cases (non-keywords)
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("1"));
 assertFalse(TokenStream.isKeyword("10"));
 assertFalse(TokenStream.isKeyword("_"));
 assertFalse(TokenStream.isKeyword("__"));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("abcd"));
 assertFalse(TokenStream.isKeyword("AbcD"));
 assertFalse(TokenStream.isKeyword("ab123"));
 assertFalse(TokenStream.isKeyword("ab_123"));
 assertFalse(TokenStream.isKeyword("A1B2"));
 assertFalse(TokenStream.isKeyword("A_B"));
 assertFalse(TokenStream.isKeyword("ab12_3"));
 assertFalse(TokenStream.isKeyword("A_B_1"));
 assertFalse(TokenStream.isKeyword("A1B2C"));
 assertFalse(TokenStream.isKeyword("A1B2_3"));
 assertFalse(TokenStream.isKeyword("A1_B2"));
 assertFalse(TokenStream.isKeyword("A_B1"));
 assertFalse(TokenStream.isKeyword("A1B2C3"));
 assertFalse(TokenStream.isKeyword("A1B2_3C"));
 assertFalse(TokenStream.isKeyword("A1_B2C"));
 assertFalse(TokenStream.isKeyword("A_B1C"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Test positive cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("abcd"));
 assertTrue(TokenStream.isJSIdentifier("AbcD"));
 assertTrue(TokenStream.isJSIdentifier("ab123"));
 assertTrue(TokenStream.isJSIdentifier("ab_123"));
 assertTrue(TokenStream.isJSIdentifier("A1B2"));
 assertTrue(TokenStream.isJSIdentifier("A_B"));
 assertTrue(TokenStream.isJSIdentifier("ab12_3"));
 assertTrue(TokenStream.isJSIdentifier("A_B_1"));
 assertTrue(TokenStream.isJSIdentifier("A1B2C"));
 assertTrue(TokenStream.isJSIdentifier("A1B2_3"));
 assertTrue(TokenStream.isJSIdentifier("A1_B2"));
 assertTrue(TokenStream.isJSIdentifier("A_B1"));
 assertTrue(TokenStream.isJSIdentifier("A1B2C3"));
 assertTrue(TokenStream.isJSIdentifier("A1B2_3C"));
 assertTrue(TokenStream.isJSIdentifier("A1_B2C"));
 assertTrue(TokenStream.isJSIdentifier("A_B1C"));
 // Test negative cases
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("1"));
 assertFalse(TokenStream.isJSIdentifier("10"));
 assertFalse(TokenStream.isJSIdentifier("A1B"));
 assertFalse(TokenStream.isJSIdentifier("1B"));
 assertFalse(TokenStream.isJSIdentifier("A1B "));
 assertFalse(TokenStream.isJSIdentifier(" A1B"));
 assertFalse(TokenStream.isJSIdentifier("A1 B"));
 assertFalse(TokenStream.isJSIdentifier("A1B!"));
 assertFalse(TokenStream.isJSIdentifier("A1B@"));
 assertFalse(TokenStream.isJSIdentifier("A1B$"));
 assertFalse(TokenStream.isJSIdentifier("A1B%"));
 assertFalse(TokenStream.isJSIdentifier("A1B^"));
 assertFalse(TokenStream.isJSIdentifier("A1B&"));
 assertFalse(TokenStream.isJSIdentifier("A1B*"));
 assertFalse(TokenStream.isJSIdentifier("A1B( "));
 assertFalse(TokenStream.isJSIdentifier("A1B)"));
 assertFalse(TokenStream.isJSIdentifier("A1B["));
 assertFalse(TokenStream.isJSIdentifier("A1B]"));
 assertFalse(TokenStream.isJSIdentifier("A1B{"));
 assertFalse(TokenStream.isJSIdentifier("A1B}"));
 }
}