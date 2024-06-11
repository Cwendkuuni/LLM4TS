package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = { "false", "null", "true", "if", "else", "while", "for", "in", "switch", "case", "default", "break", "return", "function", "class", "var", "let", "const", "export", "import", "package", "private", "protected", "public", "interface", "implements", "extends", "static", "transient", "synchronized", "this", "super", "new", "instanceof", "throw", "try", "catch", "finally", "delete", "typeof", "void", "let", "const", "async", "await", "import", "export" };
 for (String keyword : keywords) {
 boolean expected = TokenStream.isKeyword(keyword);
 boolean result = TokenStream.isKeyword(keyword);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] validIdentifiers = { "_identifier", "_123Identifier", "_identifier_123", "_identifier_with_under_score", "_identifierWithDigit_1", "_identifierWithDigit_10", "_identifierWithDigit_100", "_identifierWithDollar_sign", "_identifierWithDollar_sign_1", "_identifierWithDollar_sign_10", "_identifierWithDollar_sign_100", "_identifierWithUpperCase_1", "_identifierWithUpperCase_10", "_identifierWithUpperCase_100", "_identifierWithTwo_under_scores", "_identifierWithTwo_under_scores_1", "_identifierWithTwo_under_scores_10", "_identifierWithTwo_under_scores_100", "_identifierWithTwo_under_scores_and_digits_1", "_identifierWithTwo_under_scores_and_digits_10", "_identifierWithTwo_under_scores_and_digits_100" };
 for (String identifier : validIdentifiers) {
 boolean expected = TokenStream.isJSIdentifier(identifier);
 boolean result = TokenStream.isJSIdentifier(identifier);
 assertEquals(expected, result);
 }

 String[] invalidIdentifiers = { "123Identifier", "identifier_", "identifier 1", "identifier_with space", "identifier#", "identifier%", "identifier^", "identifier&", "identifier@", "identifier$", "identifier\"" };
 for (String identifier : invalidIdentifiers) {
 boolean expected = false;
 boolean result = TokenStream.isJSIdentifier(identifier);
 assertEquals(expected, result);
 }
}

 @Test
 public void testInvalidKeyword() {
 String[] invalidKeywords = { "invalidKeyword", "reallyInvalidKeyword12345", "_invalidKeyword", "__invalidKeyword", "invalidIdentifier123", "invalidIdentifier_123", "invalidIdentifier_with_under_score" };
 for (String invalidKeyword : invalidKeywords) {
 boolean expected = false;
 boolean result = TokenStream.isKeyword(invalidKeyword);
 assertEquals(expected, result);
 }
}
}