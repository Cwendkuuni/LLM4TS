package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello World";
        int result = Utf8.encodedLength(sequence);
        assertEquals(11, result);
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "Testing Utf8";
        int result = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(12, result);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {65, 66, 67, 68, 69};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {-50, -20, -10, 80, 90};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);
    }

}