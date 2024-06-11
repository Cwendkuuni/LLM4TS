package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        boolean expected = true;
        String name = "if";
        boolean actual = TokenStream.isKeyword(name);
        assertEquals(expected, actual);
    }
}