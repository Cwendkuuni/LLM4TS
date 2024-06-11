package org.jsoup.parser; // import statements go here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    // Test for Doctype token
    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        doctypeToken.reset();
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testDoctypeTokenWithValues() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName().append("html");
        doctypeToken.getPublicIdentifier().append("MyPublicID");
        doctypeToken.getSystemIdentifier().append("/path/to/my.html");
        doctypeToken.setForceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.getName());
        assertEquals("MyPublicID", doctypeToken.getPublicIdentifier());
        assertEquals("/path/to/my.html", doctypeToken.getSystemIdentifier());
        assertTrue(doctypeToken.isForceQuirks());
        doctypeToken.reset();
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }
}

