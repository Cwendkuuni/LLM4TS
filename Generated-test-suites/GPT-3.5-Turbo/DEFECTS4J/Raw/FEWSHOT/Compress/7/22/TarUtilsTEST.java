package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {0};
        assertEquals(0, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = {49, 50, 51, 52, 53, 54, 55, 56, 57};
        assertEquals(123456789, TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = {48, 48, 55, 57, 54, 52, 51, 50, 49};
        assertEquals(78964321, TarUtils.parseOctal(buffer3, 0, buffer3.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {102, 111, 111, 0};
        assertEquals("foo", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer2 = {102, 111, 111, 98, 97, 114, 0};
        assertEquals("foobar", TarUtils.parseName(buffer2, 0, buffer2.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo";
        byte[] buf = new byte[3];
        int offset = 0;
        int length = buf.length;
        assertEquals(3, TarUtils.formatNameBytes(name, buf, offset, length));
        byte[] expected = {102, 111, 111};
        assertArrayEquals(expected, buf);

        String name2 = "";
        byte[] buf2 = new byte[3];
        int offset2 = 0;
        int length2 = buf.length;
        assertEquals(3, TarUtils.formatNameBytes(name2, buf2, offset2, length2));
        byte[] expected2 = {0, 0, 0};
        assertArrayEquals(expected2, buf2);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[9];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {49, 55, 54, 71, 53, 67, 51, 57, 0};
        assertArrayEquals(expected, buffer);

        long value2 = 0;
        byte[] buffer2 = new byte[1];
        int offset2 = 0;
        int length2 = buffer2.length;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        byte[] expected2 = {48};
        assertArrayEquals(expected2, buffer2);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        assertEquals(10, TarUtils.formatOctalBytes(value, buf, offset, length));
        byte[] expected = {49, 55, 54, 71, 53, 67, 51, 57, 32, 0};
        assertArrayEquals(expected, buf);

        long value2 = 0;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        assertEquals(10, TarUtils.formatOctalBytes(value2, buf2, offset2, length2));
        byte[] expected2 = {48, 32, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected2, buf2);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[11];
        int offset = 0;
        int length = buf.length;
        assertEquals(11, TarUtils.formatLongOctalBytes(value, buf, offset, length));
        byte[] expected = {49, 55, 54, 71, 53, 67, 51, 57, 32, 0, 0};
        assertArrayEquals(expected, buf);

        long value2 = 0;
        byte[] buf2 = new byte[11];
        int offset2 = 0;
        int length2 = buf2.length;
        assertEquals(11, TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2));
        byte[] expected2 = {48, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected2, buf2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        assertEquals(10, TarUtils.formatCheckSumOctalBytes(value, buf, offset, length));
        byte[] expected = {49, 55, 54, 71, 53, 67, 51, 57, 0, 32};
        assertArrayEquals(expected, buf);

        long value2 = 0;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        assertEquals(10, TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2));
        byte[] expected2 = {48, 32, 0, 0, 0, 0, 0, 0, 0, 32};
        assertArrayEquals(expected2, buf2);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 55, 54, 71, 53, 67, 51, 57, 32, 0, 0};
        assertEquals(825, TarUtils.computeCheckSum(buf));

        byte[] buf2 = {48, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(32, TarUtils.computeCheckSum(buf2));
    }
}