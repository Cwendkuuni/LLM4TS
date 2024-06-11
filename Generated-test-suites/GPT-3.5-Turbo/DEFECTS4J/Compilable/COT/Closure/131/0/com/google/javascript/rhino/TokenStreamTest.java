package com.google.javascript.rhino;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test 1: Check a valid keyword
        assertTrue(TokenStream.isKeyword("if"));

        // Test 2: Check an invalid keyword
        assertFalse(TokenStream.isKeyword("banana"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test 3: Check a valid JavaScript identifier
        assertTrue(TokenStream.isJSIdentifier("myVariable"));

        // Test 4: Check an invalid JavaScript identifier
        assertFalse(TokenStream.isJSIdentifier("123Variable"));
    }
}