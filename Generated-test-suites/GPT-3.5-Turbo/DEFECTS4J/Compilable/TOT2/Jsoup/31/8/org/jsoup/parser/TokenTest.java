package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getName());
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getPublicIdentifier());
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getSystemIdentifier());
        // Add more test cases for edge cases and error scenarios
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        // Add more test cases for edge cases and error scenarios
    }

    // Add more test cases for other methods in the Token class

}