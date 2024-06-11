package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {49, 50, 51, 52}; // ASCII values for "1234"
        assertEquals(1234, TarUtils.parseOctal(buffer, 0, buffer.length));

        // Edge case - buffer length exceeds maximum number of bytes to parse
        byte[] buffer2 = {49, 50, 51, 52, 53}; // ASCII values for "12345"
        assertEquals(1234, TarUtils.parseOctal(buffer2, 0, 4));

        // Error scenario - invalid octal digit
        byte[] buffer3 = {49, 50, 56, 52}; // ASCII values for "1284"
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
        });
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = {65, 66, 67, 0}; // ASCII values for "ABC" with null terminator
        assertEquals("ABC", TarUtils.parseName(buffer, 0, buffer.length));

        // Edge case - buffer length exceeds maximum number of bytes to parse
        byte[] buffer2 = {65, 66, 67, 68, 0}; // ASCII values for "ABCD" with null terminator
        assertEquals("ABC", TarUtils.parseName(buffer2, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("ABC", buf, 0, 3);
        byte[] expected = {65, 66, 67, 0, 0}; // ASCII values for "ABC" with two null terminators
        assertArrayEquals(expected, buf);

        // Edge case - name longer than buffer
        byte[] buf2 = new byte[5];
        int offset2 = TarUtils.formatNameBytes("ABCD", buf2, 0, 3);
        byte[] expected2 = {65, 66, 67, 0, 0}; // ASCII values for "ABC" with two null terminators
        assertArrayEquals(expected2, buf2);
    }

    // More test cases for remaining methods can be added here

}