package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 private static final String[] KEYWORDS = {
         "abstract", "arguments", "await", "boolean", "break", "byte",
         "case", "catch", "char", "class", "const", "continue", "debugger",
         "default", "delete", "do", "double", "else", "enum", "eval",
         "export", "extends", "false", "final", "finally", "float", "for",
         "function", "goto", "if", "implements", "import", "in", "instanceof",
         "int", "interface", "let", "long", "native", "new", "null", "package",
         "private", "protected", "public", "return", "short", "static",
         "super", "switch", "synchronized", "this", "throw", "throws",
         "transient", "true", "try", "typeof", "var", "void", "volatile",
         "while", "with"
 };

 @Test
 public void testIsKeyword() {
     for (String keyword : KEYWORDS) {
         assertTrue(isKeyword(keyword));
     }
 }

 @Test
 public void testIsNotKeyword() {
     String[] nonKeywords = { "abcd", "ab_cd", "1234", "MyClass", "myFunction" };

     for (String nonKeyword : nonKeywords) {
         assertFalse(isKeyword(nonKeyword));
     }
 }

 @Test
 public void testIsJSIdentifier() {
     String[] identifiers = { "_abc123", "myIdentifier123", "_myIdentifier",
         "my_identifier_123", "ident123", "_ident123_", "ab_cd_ef_gh_ijk" };

     for (String identifier : identifiers) {
         assertTrue(isJSIdentifier(identifier));
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
     assertFalse(isJSIdentifier(null));
     assertFalse(isKeyword(null));
 }
}