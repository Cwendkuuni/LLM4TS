import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = new String[]{"if", "else", "for", "while", "return", "void", "int", "float", "double", "boolean", "true", "false", "null", "class", "package", "import", "export", "public", "private", "protected", "interface", "implements", "extends", "static", "static final", "abstract", "final", "throws", "synchronized"};

 for (String keyword : keywords) {
 assertTrue(isKeyword(keyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] identifiers = new String[]{"validIdentifier1", "validIdentifier2", "ValidIdentifier3", "_identifier", "identifier_1", "identifierWithDigits_123", "identifierWith_underscores", "identifierWithDollarSign_$identifier", "identifierWithCapitalLetter_CapitalIdentifier"};

 for (String identifier : identifiers) {
 assertTrue(isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_Invalid() {
 String[] invalidIdentifiers = new String[]{"invalidIdentifier1", "invalidIdentifier2", "1identifier", "identifier-", "identifier\t"};

 for (String identifier : invalidIdentifiers) {
 assertFalse(isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_EmptyString() {
 assertFalse(isJSIdentifier(""));
 }

 @Test(expected = NullPointerException.class)
 public void testIsJSIdentifier_Null() {
 assertFalse(isJSIdentifier(null));
 }

 @Test
 public void testIsKeyword_Invalid() {
 assertFalse(isKeyword("invalidKeyword"));
 }

 @Test(expected = NullPointerException.class)
 public void testIsKeyword_Null() {
 assertFalse(isKeyword(null));
 }
}