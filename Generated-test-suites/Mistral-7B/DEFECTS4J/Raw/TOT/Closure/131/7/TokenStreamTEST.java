import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_SimpleKeywords() {
 String[] keywords = {"if", "else", "for", "while", "switch", "case", "default", "try", "catch", "throw", "return", "break", "continue", "new", "class", "var", "let", "const", "export", "import", "this", "package", "public", "private", "protected", "static", "interface", "implements", "extends", "abstract", "transient", "synchronized"};
 for (String keyword : keywords) {
 assertTrue("'" + keyword + "' should be a keyword", isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_ReservedWords() {
 String[] reservedWords = {"byte", "boolean", "short", "char", "int", "long", "float", "double", "void", "null", "true", "false", "goto", "break", "continue", "if", "else", "for", "while", "switch", "case", "default", "try", "catch", "finally", "throw", "return", "this", "new", "class", "var", "let", "const", "export", "import"};
 for (String reservedWord : reservedWords) {
 assertTrue("'" + reservedWord + "' should be a reserved word", isKeyword(reservedWord));
 }
 }

 @Test
 public void testIsKeyword_InvalidStrings() {
 String[] invalidStrings = {"invalid1", "invalid2", "invalid3", "invalid4", "invalid5", "invalid6", "invalid7", "invalid8", "invalid9", "invalid10", "invalid11", "invalid12", "invalid13", "invalid14"};
 for (String invalidString : invalidStrings) {
 assertFalse("'" + invalidString + "' should not be a keyword", isKeyword(invalidString));
 }
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 String[] validIdentifiers = {"ident1", "identifier123", "identifier_123", "identifier_with_number_123", "identifierWithCamelCase", "identifierWithCapitalLetters", "identifierWithDollarSign_1", "identifierWithDollarSign_123"};
 for (String validIdentifier : validIdentifiers) {
 assertTrue("'" + validIdentifier + "' should be a valid identifier", isJSIdentifier(validIdentifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 String[] invalidIdentifiers = {"invalid1", "invalidIdentifier123", "invalid_identifier", "invalid_identifier_123", "invalidIdentifierWithNumber123", "invalidIdentifierWithCamelCase", "invalidIdentifierWithCapitalLetters", "invalidIdentifierWithDollarSign_1", "invalidIdentifierWithDollarSign_123", "invalidIdentifier_with_space", "invalidIdentifier_with_tab", "invalidIdentifier_with_newline"};
 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse("'" + invalidIdentifier + "' should not be a valid identifier", isJSIdentifier(invalidIdentifier));
 }
 }
}