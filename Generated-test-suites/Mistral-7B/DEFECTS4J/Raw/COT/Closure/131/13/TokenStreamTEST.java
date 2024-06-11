package com.google.javascript.rhino.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_SimpleKeywords() {
        String[] keywords = new String[]{"if", "else", "while", "for", "switch", "case", "default", "break", "continue", "return", "void", "boolean", "false", "null", "true", "package", "import", "export", "public", "private", "protected", "abstract", "interface", "implements", "extends", " instanceof", "new", "this", "super", "class", "let", "const", "const", "let", "var", "function", "static", "final", "native", "synchronized", "yield"};

        for (String keyword : keywords) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }

    @Test
    public void testIsKeyword_InvalidKeywords() {
        String[] invalidKeywords = new String[]{"abcd", "1234", "myKeyword", "my_keyword", "my_long_keyword"};

        for (String invalidKeyword : invalidKeywords) {
            assertFalse(TokenStream.isKeyword(invalidKeyword));
        }
    }

    @Test
    public void testIsJSIdentifier_ValidIdentifiers() {
        String[] validIdentifiers = new String[]{"identifier1", "_identifier2", "identifier_3", "identifier_with_digits_123", "identifierWith_underscores_and_digits_123_abcd", "identifierWith_only_digits_123"};

        for (String validIdentifier : validIdentifiers) {
            assertTrue(TokenStream.isJSIdentifier(validIdentifier));
        }
    }

    @Test
    public void testIsJSIdentifier_InvalidIdentifiers() {
        String[] invalidIdentifiers = new String[]{"invalidIdentifier1", "1identifier", "_invalidIdentifier", "identifier-with-hyphen", "identifier_with_space", "identifier with space", "identifier with tab", "identifier with newline", "identifier with carriage return"};

        for (String invalidIdentifier : invalidIdentifiers) {
            assertFalse(TokenStream.isJSIdentifier(invalidIdentifier));
        }
    }
}