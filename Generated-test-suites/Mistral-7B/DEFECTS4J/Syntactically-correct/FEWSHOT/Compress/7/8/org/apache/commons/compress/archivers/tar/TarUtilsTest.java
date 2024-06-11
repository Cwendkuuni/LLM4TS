package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.tar.TarUtils;

import java.util.ByteArrayOutputStream;
import java.util.Random;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        new Random().nextBytes(buffer);

        long expected = new Random().nextLong();
        TarUtils.formatUnsignedOctalString(expected, buffer, 0, buffer.length - 1);

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalWithInvalidInput() {
        byte[] buffer = new byte[10];
        new Random().nextBytes(buffer);

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertNull(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "testName".getBytes();
        String expected = "testName";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = "testName".getBytes();
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = new Random().nextLong();
        byte[] buffer = new byte[10];
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, 0, length);

        long result = TarUtils.parseOctal(buffer, 0, length);
        assertEquals(value, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = new Random().nextLong();
        byte[] buffer = new byte[10];
        int length = buffer.length;

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, length);

        long result = TarUtils.parseOctal(buffer, 0, offset);
        assertEquals(value, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = new Random().nextLong();
        byte[] buffer = new byte[10];
        int length = buffer.length;

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, length);

        long result = TarUtils.parseOctal(buffer, 0, offset);
        assertEquals(value, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = new Random().nextLong();
        byte[] buffer = new byte[10];
        int length = buffer.length;

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, length);

        long result = TarUtils.parseOctal(buffer, 0, offset);
        assertEquals(value, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "test".getBytes();
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }
}