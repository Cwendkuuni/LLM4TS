
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test 1: Empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test 2: ASCII characters only
        assertEquals(5, Utf8.encodedLength("hello"));

        // Test 3: Non-ASCII characters
        assertEquals(8, Utf8.encodedLength("héllo"));

        // Test 4: Surrogate pair
        assertEquals(5, Utf8.encodedLength("hello\uD83D\uDE00"));

        // Test 5: Maximum possible length
        assertEquals(47, Utf8.encodedLength("12345678901234567890123456789012345678901234567890"));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = new byte[]{0x68, 0x65, 0x6C, 0x6C, 0x6F};
        byte[] bytes2 = new byte[]{(byte) 0xC3, (byte) 0xA9, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
        byte[] bytes3 = new byte[]{(byte) 0xED, (byte) 0xA0, (byte) 0xBD, (byte) 0xED, (byte) 0xB8};
        byte[] bytes4 = new byte[]{(byte) 0xED, (byte) 0xA0, (byte) 0xBD, (byte) 0xED, (byte) 0xB9, (byte) 0x80};
        byte[] bytes5 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80};
        byte[] bytes6 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80, (byte) 0x68};

        // Test 1: Well-formed ASCII bytes
        assertTrue(Utf8.isWellFormed(bytes1));

        // Test 2: Well-formed non-ASCII bytes
        assertTrue(Utf8.isWellFormed(bytes2));

        // Test 3: Malformed bytes - incomplete surrogate pair
        assertFalse(Utf8.isWellFormed(bytes3));

        // Test 4: Malformed bytes - invalid UTF-8 encoding
        assertFalse(Utf8.isWellFormed(bytes4));

        // Test 5: Well-formed surrogate pair
        assertTrue(Utf8.isWellFormed(bytes5));

        // Test 6: Malformed bytes - ASCII followed by a surrogate pair
        assertFalse(Utf8.isWellFormed(bytes6));
    }

}
