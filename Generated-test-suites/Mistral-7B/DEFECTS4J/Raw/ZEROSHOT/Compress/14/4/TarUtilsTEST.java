package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtilities;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    private static final byte[] TEST_BYTE_ARRAY = new byte[] {
            0x66, 0x72, 0x6d, 0x61, 0x20, 0x73, 0x74, 0x72, 0x6b, 0x65, 0x79, 0x0, // tar file
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // header length: 0
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // name length: 0
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // mode: 00000000
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // uid: 0
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // gid: 0
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, // size: 0
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 // mtime: 0
    };

    @Test
    public void testParseOctal() {
        long num = TarUtils.parseOctal(TEST_BYTE_ARRAY, 10, 5);
        assertEquals("Parsed incorrect octal number", 024, num);

        try {
            TarUtils.parseOctal(TEST_BYTE_ARRAY, 10, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            TarUtils.parseOctal(TEST_BYTE_ARRAY, 10, 8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer = new byte[1];
        buffer[0] = 0;
        try {
            TarUtils.parseOctal(buffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer2 = new byte[1];
        buffer2[0] = 1;
        try {
            TarUtils.parseOctal(buffer2, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        long num = TarUtils.parseOctalOrBinary(TEST_BYTE_ARRAY, 10, 12);
        assertEquals("Parsed incorrect binary number", 2052651456L, num);

        try {
            TarUtils.parseOctalOrBinary(TEST_BYTE_ARRAY, 10, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        assertTrue(TarUtils.parseBoolean(TEST_BYTE_ARRAY, 15));
    }

    @Test
    public void testParseName() {
        String name = TarUtils.parseName(TEST_BYTE_ARRAY, 1, 13);
        assertEquals("Parsed incorrect name", "testfile", name);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testfile";
        byte[] buffer = new byte[15];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 15);
        String result = new String(buffer, 0, offset);
        assertEquals("Failed to format name bytes", "testfile", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long num = 30;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(num, buffer, 0, 11);
        String result = new String(buffer, 0, 11);
        assertEquals("Failed to format unsigned octal string", "42\0 \0", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long num = 30;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(num, buffer, 0, 13);
        String result = new String(buffer, 0, offset);
        assertEquals("Failed to format octal bytes", "040222\0 \0", result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long num = 30;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(num, buffer, 0, 13);
        String result = new String(buffer, 0, offset);
        assertEquals("Failed to format long octal or binary bytes", "0004022200\0 \0", result);
    }

    @Test
    public void testComputeCheckSum() {
        long checksum = TarUtils.computeCheckSum(TEST_BYTE_ARRAY);
        assertEquals("Failed to compute correct checksum", 307627541L, checksum);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long num = 30;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(num, buffer, 0, 13);
        String result = new String(buffer, 0, offset);
        assertEquals("Failed to format checksum octal bytes", "0004022200\0 \0", result);
    }
}