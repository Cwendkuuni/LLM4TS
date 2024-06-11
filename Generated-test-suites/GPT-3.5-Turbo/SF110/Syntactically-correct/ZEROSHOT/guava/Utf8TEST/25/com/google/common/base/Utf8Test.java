package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test case 1
        assertEquals(4, Utf8.encodedLength("test"));

        // Test case 2
        assertEquals(10, Utf8.encodedLength("test string"));

        // Test case 3
        assertEquals(6, Utf8.encodedLength("测试")); // Non-ascii characters
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test case 1
        assertEquals(2, Utf8.encodedLengthGeneral("A", 0));

        // Test case 2
        assertEquals(2, Utf8.encodedLengthGeneral("AB", 0));

        // Test case 3
        assertEquals(6, Utf8.encodedLengthGeneral("测试", 0)); // Non-ascii characters
    }

    @Test
    public void testIsWellFormed() {
        // Test case 1
        byte[] bytes1 = { -61, -120, -30, -126, -84 };
        assertTrue(Utf8.isWellFormed(bytes1));

        // Test case 2
        byte[] bytes2 = { -61, -120, -30, -126, -84, 32, 116, 101, 115, 116 };
        assertTrue(Utf8.isWellFormed(bytes2));

        // Test case 3
        byte[] bytes3 = { -61, -120, -30, -126, -84, 32, 116, 101, 115, 116, 32, 115, 116, 114, 105, 110, 103 };
        assertTrue(Utf8.isWellFormed(bytes3));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test case 1
        byte[] bytes1 = { -61, -120, -30, -126, -84 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, bytes1.length));

        // Test case 2
        byte[] bytes2 = { -61, -120, -30, -126, -84, 32, 116, 101, 115, 116 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, bytes2.length));

        // Test case 3
        byte[] bytes3 = { -61, -120, -30, -126, -84, 32, 116, 101, 115, 116, 32, 115, 116, 114, 105, 110, 103 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes3, 0, bytes3.length));
    }
}