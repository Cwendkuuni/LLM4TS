package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertEquals(true, TokenStream.isKeyword("if"));
        assertEquals(true, TokenStream.isKeyword("else"));
        // Add more test cases for other keywords
    }
}