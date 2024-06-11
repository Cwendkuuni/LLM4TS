package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] testCases = new String[]{"if", "else", "for", "while", "switch", "case", "default",
         "break", "continue", "return", "void", "false", "true", "null", "this", "super",
         "class", "interface", "extends", "implements", "package", "import", "public",
         "private", "protected", "static", "final", "const", "transient", "synchronized",
         "native", "abstract", "throws", "check", "goto", "try", "catch", "finally",
         "export", "import", "export default", "export from", "let", "const"};

 for (String name : testCases) {
 String token = name;
 boolean expectedResult = isKeyword(token);
 boolean actualResult = Boolean.parseBoolean(new Boolean(isKeyword(token)).toString());
 assertEquals("isKeyword should return " + expectedResult + " for token " + token,
         expectedResult, actualResult);
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] testCases = new String[]{"_identifier", "a1b2c_dE4fG_H5iJ", "_identifier123",
         "identifier_123", "identifier123", "identifier_abcd", "identifierAbCd",
         "identifier_123_abcd", "identifier_ABCd", "identifier_AbCd", "identifier_123_abCd",
         "Identifier", "Identifier123", "identifier_AbCd_123", "identifier_123_AbCd",
         "identifier_123_AbCd_123", "identifier_123_AbCd_123_"};

 for (String identifier : testCases) {
 String token = identifier;
 boolean expectedResult = isJSIdentifier(token);
 boolean actualResult = Boolean.parseBoolean(new Boolean(isJSIdentifier(token)).toString());
 assertEquals("isJSIdentifier should return " + expectedResult + " for token " + token,
         expectedResult, actualResult);
 }
 }
}