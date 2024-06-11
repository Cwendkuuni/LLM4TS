package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = "   123".getBytes();
        long actual = TarUtils.parseOctal(buffer, 0, 7);
        assertEquals(83, actual);

        // Edge case - buffer is all NULs
        buffer = new byte[5];
        actual = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(0, actual);

        // Error scenario - invalid byte
        buffer = "A".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = "hello.tar".getBytes();
        String actual = TarUtils.parseName(buffer, 0, 9);
        assertEquals("hello.tar", actual);

        // Edge case - buffer is all NULs
        buffer = new byte[5];
        actual = TarUtils.parseName(buffer, 0, 5);
        assertEquals("", actual);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name = "file.tar";
        byte[] buffer = new byte[8];
        int actual = TarUtils.formatNameBytes(name, buffer, 0, 8);
        assertArrayEquals("file.tar\0".getBytes(), buffer);
        assertEquals(8, actual);

        // Edge case - name is longer than buffer
        name = "long_name.tar";
        buffer = new byte[8];
        actual = TarUtils.formatNameBytes(name, buffer, 0, 8);
        assertArrayEquals("long_nam".getBytes(), buffer);
        assertEquals(8, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Typical use case
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
        assertArrayEquals("456\0".getBytes(), buffer);

        // Edge case - value is 0
        value = 0;
        buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
        assertArrayEquals("000".getBytes(), buffer);

        // Edge case - value is greater than buffer length
        value = 1234;
        buffer = new byte[3];
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // Typical use case
        long value = 123;
        byte[] buffer = new byte[5];
        int actual = TarUtils.formatOctalBytes(value, buffer, 0, 5);
        assertArrayEquals("456 \0".getBytes(), buffer);
        assertEquals(5, actual);

        // Error scenario - value (and trailer) will not fit in the buffer
        value = 12345;
        buffer = new byte[4];
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, 4);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Typical use case
        long value = 123;
        byte[] buffer = new byte[4];
        int actual = TarUtils.formatLongOctalBytes(value, buffer, 0, 4);
        assertArrayEquals("345 ".getBytes(), buffer);
        assertEquals(4, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Typical use case
        long value = 123;
        byte[] buffer = new byte[5];
        int actual = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 5);
        assertArrayEquals("456  ".getBytes(), buffer);
        assertEquals(5, actual);

        // Error scenario - value (and trailer) will not fit in the buffer
        value = 12345;
        buffer = new byte[4];
        try {
            TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 4);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testComputeCheckSum() {
        // Typical use case
        byte[] buffer = "hello".getBytes();
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(532, actual);

        // Edge case - buffer is all NULs
        buffer = new byte[5];
        actual = TarUtils.computeCheckSum(buffer);
        assertEquals(0, actual);
    }

}