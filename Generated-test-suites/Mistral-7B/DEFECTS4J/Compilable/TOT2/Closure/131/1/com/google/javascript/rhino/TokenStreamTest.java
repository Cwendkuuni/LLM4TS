package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        String keyword = "if";
        boolean expected = true;
        boolean actual = TokenStream.isKeyword(keyword);
        assertEquals(expected, actual);
    }
}