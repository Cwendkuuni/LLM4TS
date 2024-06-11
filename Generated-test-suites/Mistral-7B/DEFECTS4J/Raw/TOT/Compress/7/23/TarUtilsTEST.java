package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Octal value should be parsed correctly", 01234567L, result);

        // Test with leading spaces
        result = TarUtils.parseOctal(new byte[]{' ', '0', '1', '2', '3', '4', '5', '6', '7', '8'}, 0, buffer.length);
        assertEquals("Octal value should be parsed correctly with leading spaces", 01234567L, result);

        // Test with trailing spaces
        result = TarUtils.parseOctal(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', ' '}, 0, buffer.length);
        assertEquals("Octal value should be parsed correctly with trailing spaces", 01234567L, result);

        // Test with invalid input
        byte[] invalidInput = new byte[]{'0', '1', '2', 'A', '3', '4', '5', '6', '7'};
        try {
            result = TarUtils.parseOctal(invalidInput, 0, invalidInput.length);
            fail("Expected IllegalArgumentException with invalid input");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'E', 'n', 't', 'r', 'y', ' ', 'p', 'a', 't', 'h', '/', 'm', 'y', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r', '.\0'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Entry name should be parsed correctly", "tar Entry.tar", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar Entry.tar";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = new byte[]{'t', 'a', 'r', ' ', 'E', 'n', 't', 'r', 'y', '\0'};
        assertArrayEquals("Formatted name bytes should match expected", expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01234567L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '\0'};
        assertArrayEquals("Formatted unsigned octal string should match expected", expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01234567L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', ' ', '0'};
        assertArrayEquals("Formatted octal bytes should match expected", expected, buffer, 0, expected.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'E', 'n', 't', 'r', 'y', ' ', 'p', 'a', 't', 'h', '.', 't', 'a', 'r', '\0'};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals("Computed checksum should match expected", checksum, computedChecksum);
    }
}