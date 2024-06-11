package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello";
        int expectedResult = 5;
        int result = Utf8.encodedLength(sequence);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        assertTrue(Utf8.isWellFormed(bytes));
    }
}