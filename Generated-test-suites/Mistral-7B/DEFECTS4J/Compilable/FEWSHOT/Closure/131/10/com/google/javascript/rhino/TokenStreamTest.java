package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_emptyString() {
 assertTrue(TokenStream.isKeyword(""));
 }

 @Test
 public void testIsKeyword_false() {
 assertFalse(TokenStream.isKeyword("abcd"));
 }

 @Test
 public void testIsKeyword_true_1letter() {
 testKeyword("break", true);
 testKeyword("case", true);
 testKeyword("catch", true);
 testKeyword("class", true);
 testKeyword("const", true);
 testKeyword("continue", true);
 testKeyword("defaulr", false); // should be false due to misspelled "default"
 testKeyword("delete", true);
 testKeyword("do", true);
 testKeyword("else", true);
 testKeyword("export", true);
 testKeyword("extends", true);
 testKeyword("false", true);
 testKeyword("finally", true);
 testKeyword("for", true);
 testKeyword("function", true);
 testKeyword("goto", true);
 testKeyword("if", true);
 testKeyword("implements", true);
 testKeyword("in", true);
 testKeyword("instanceof", true);
 testKeyword("int", false); // not a keyword, only a type
 testKeyword("new", true);
 testKeyword("null", true);
 testKeyword("package", true);
 testKeyword("private", true);
 testKeyword("public", true);
 testKeyword("protected", true);
 testKeyword("return", true);
 testKeyword("short", false); // not a keyword, only a type
 testKeyword("static", true);
 testKeyword("super", true);
 testKeyword("switch", true);
 testKeyword("synchronized", true);
 testKeyword("this", true);
 testKeyword("throw", true);
 testKeyword("throwError", false); // not a keyword
 testKeyword("transient", true);
 testKeyword("true", true);
 testKeyword("try", true);
 testKeyword("typeof", true);
 testKeyword("var", true);
 testKeyword("void", true);
 testKeyword("volatile", true);
 testKeyword("while", true);
 testKeyword("with", true);
 testKeyword("yield", false); // not a keyword
 }

 @Test
 public void testIsKeyword_true_2letters() {
 testKeyword("abstract", true);
 testKeyword("boolean", false); // not a keyword, only a type
 testKeyword("byte", false); // not a keyword, only a type
 testKeyword("case", true);
 testKeyword("catch", true);
 testKeyword("char", false); // not a keyword, only a type
 testKeyword("class", true);
 testKeyword("const", true);
 testKeyword("continue", true);
 testKeyword("debugger", true);
 testKeyword("default", true);
 testKeyword("delete", true);
 testKeyword("do", true);
 testKeyword("double", false); // not a keyword, only a type
 testKeyword("else", true);
 testKeyword("enum", false); // not a keyword
 testKeyword("eval", false); // not a keyword
 testKeyword("export", true);
 testKeyword("extends", true);
 testKeyword("false", true);
 testKeyword("final", true);
 testKeyword("finally", true);
 testKeyword("float", false); // not a keyword, only a type
 testKeyword("for", true);
 testKeyword("function", true);
 testKeyword("goto", true);
 testKeyword("if", true);
 testKeyword("implements", true);
 testKeyword("import", true);
 testKeyword("in", true);
 testKeyword("instanceof", true);
 testKeyword("int", false); // not a keyword, only a type
 testKeyword("interface", true);
 testKeyword("let", false); // not a keyword
 testKeyword("long", false); // not a keyword, only a type
 testKeyword("native", true);
 testKeyword("new", true);
 testKeyword("null", true);
 testKeyword("package", true);
 testKeyword("private", true);
 testKeyword("protected", true);
 testKeyword("public", true);
 testKeyword("return", true);
 testKeyword("short", false); // not a keyword, only a type
 testKeyword("static", true);
 testKeyword("super", true);
 testKeyword("switch", true);
 testKeyword("synchronized", true);
 testKeyword("this", true);
 testKeyword("throw", true);
 testKeyword("throws", true);
 testKeyword("transient", true);
 testKeyword("true", true);
 testKeyword("try", true);
 testKeyword("typeof", true);
 testKeyword("var", true);
 testKeyword("void", true);
 testKeyword("volatile", true);
 testKeyword("while", true);
 testKeyword("with", true);
 testKeyword("yield", false); // not a keyword
 }

 @Test
 public void testIsKeyword_true_3letters() {
 testKeyword("abstract", true);
 testKeyword("boolean", false); // not a keyword, only a type
 testKeyword("break", true);
 testKeyword("byte", false); // not a keyword, only a type
 testKeyword("case", true);
 testKeyword("catch", true);
 testKeyword("char", false); // not a keyword, only a type
 testKeyword("class", true);
 testKeyword("const", true);
 testKeyword("continue", true);
 testKeyword("debugger", true);
 testKeyword("default", true);
 testKeyword("delete", true);
 testKeyword("do", true);
 testKeyword("double", false); // not a keyword, only a type
 testKeyword("else", true);
 testKeyword("enum", false); // not a keyword
 testKeyword("eval", false); // not a keyword
 testKeyword("export", true);
 testKeyword("extends", true);
 testKeyword("false", true);
 testKeyword("final", true);
 testKeyword("finally", true);
 testKeyword("float", false); // not a keyword, only a type
 testKeyword("for", true);
 testKeyword("function", true);
 testKeyword("goto", true);
 testKeyword("if", true);
 testKeyword("implements", true);
 testKeyword("import", true);
 testKeyword("in", true);
 testKeyword("instanceof", true);
 testKeyword("int", false); // not a keyword, only a type
 testKeyword("interface", true);
 testKeyword("let", false); // not a keyword
 testKeyword("long", false); // not a keyword, only a type
 testKeyword("native", true);
 testKeyword("new", true);
 testKeyword("null", true);
 testKeyword("package", true);
 testKeyword("private", true);
 testKeyword("protected", true);
 testKeyword("public", true);
 testKeyword("return", true);
 testKeyword("short", false); // not a keyword, only a type
 testKeyword("static", true);
 testKeyword("super", true);
 testKeyword("switch", true);
 testKeyword("synchronized", true);
 testKeyword("this", true);
 testKeyword("throw", true);
 testKeyword("throws", true);
 testKeyword("transient", true);
 testKeyword("true", true);
 testKeyword("try", true);
 testKeyword("typeof", true);
 testKeyword("var", true);
 testKeyword("void", true);
 testKeyword("volatile", true);
 testKeyword("while", true);
 testKeyword("with", true);
 testKeyword("yield", false); // not a keyword
 }

 @Test
 public void testIsKeyword_true_multipleLetters() {
 testKeyword("abstract", true);
 testKeyword("boolean", false); // not a keyword, only a type
 testKeyword("breakpoint", false);
 testKeyword("caseSensitive", false);
 testKeyword("catchError", false);
 testKeyword("classDeclaration", false);
 testKeyword("constValue", false);
 testKeyword("continueLoop", false);
 testKeyword("debuggerStatement", true);
 testKeyword("defaultCase", true);
 testKeyword("deleteProperty", true);
 testKeyword("doWhile", true);
 testKeyword("doubleColon", false);
 testKeyword("elseIf", true);
 testKeyword("enumConstant", false);
 testKeyword("evalScript", false);
 testKeyword("exportDefault", true);
 testKeyword("extendsKeyword", true);
 testKeyword("falseValue", true);
 testKeyword("finalVariable", true);
 testKeyword("finallyBlock", true);
 testKeyword("floatLiteral", false);
 testKeyword("forInStatement", true);
 testKeyword("functionDeclaration", true);
 testKeyword("gotoStatement", true);
 testKeyword("ifStatement", true);
 testKeyword("implementsInterface", true);
 testKeyword("importDeclaration", true);
 testKeyword("inOperator", true);
 testKeyword("instanceofOperator", true);
 testKeyword("interfaceKeyword", true);
 testKeyword("letKeyword", false);
 testKeyword("longLiteral", false);
 testKeyword("nativeFunction", false);
 testKeyword("newKeyword", true);
 testKeyword("nullKeyword", true);
 testKeyword("packageName", true);
 testKeyword("privateKeyword", true);
 testKeyword("protectedKeyword", true);
 testKeyword("publicKeyword", true);
 testKeyword("returnStatement", true);
 testKeyword("shortLiteral", false);
 testKeyword("staticKeyword", true);
 testKeyword("superKeyword", true);
 testKeyword("switchStatement", true);
 testKeyword("synchronizedStatement", true);
 testKeyword("thisKeyword", true);
 testKeyword("throwStatement", true);
 testKeyword("throwsKeyword", true);
 testKeyword("transientVariable", true);
 testKeyword("trueValue", true);
 testKeyword("tryStatement", true);
 testKeyword("typeofOperator", true);
 testKeyword("varKeyword", true);
 testKeyword("voidKeyword", true);
 testKeyword("volatileKeyword", true);
 testKeyword("whileStatement", true);
 testKeyword("withStatement", true);
 testKeyword("yieldStatement", false);
 }

 @Test
 public void testIsJSIdentifier() {
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("Abc"));
 assertTrue(TokenStream.isJSIdentifier("A_bc"));
 assertTrue(TokenStream.isJSIdentifier("123"));
 assertTrue(TokenStream.isJSIdentifier("_123"));
 assertTrue(TokenStream.isJSIdentifier("ab_cde"));
 assertTrue(TokenStream.isJSIdentifier("ab_cde_"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_123"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_123_"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_1_23"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_1_23_"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_123_1"));
 assertTrue(TokenStream.isJSIdentifier("ab_c_de_123_1_"));
 assertFalse(TokenStream.isJSIdentifier("1234")); // number not a valid identifier
 assertFalse(TokenStream.isJSIdentifier("ab_cde5")); // number not part of identifier
 assertFalse(TokenStream.isJSIdentifier("this.isKeyword")); // contains a keyword
 assertFalse(TokenStream.isJSIdentifier("true = false")); // contains assignment
 }

 private void testKeyword(String keyword, boolean expected) {
 String s = keyword;
 assertTrue("Expected '" + expected + "' for keyword '" + s + "'",
            TokenStream.isKeyword(s) == expected);
 }
}