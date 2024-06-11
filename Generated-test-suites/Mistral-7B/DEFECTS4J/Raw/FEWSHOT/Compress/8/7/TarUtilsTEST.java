package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testBuffer1 = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '0'};
        assertEquals(01777770L, TarUtils.parseOctal(testBuffer1, 0, testBuffer1.length));

        byte[] testBuffer2 = new byte[]{'0', '1', '0', '1', '0', '1', '0', '1', '0', '1', '0'};
        assertEquals(012345675L, TarUtils.parseOctal(testBuffer2, 0, testBuffer2.length));

        byte[] testBuffer3 = new byte[]{'0'};
        assertNull(TarUtils.parseOctal(testBuffer3, 0, testBuffer3.length));

        byte[] testBuffer4 = new byte[]{'1', '2', '3', '4', '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testBuffer4, 0, testBuffer4.length));
    }

    @Test
    public void testParseName() {
        byte[] testBuffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 0};
        assertEquals("hello", TarUtils.parseName(testBuffer1, 0, testBuffer1.length));

        byte[] testBuffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\0'};
        assertEquals("hello world", TarUtils.parseName(testBuffer2, 0, testBuffer2.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(name.getBytes(), Arrays.copyOfRange(buf, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] testBuffer1 = new byte[10];
        TarUtils.formatUnsignedOctalString(77, testBuffer1, 0, testBuffer1.length);
        assertEquals("0177", new String(testBuffer1));

        byte[] testBuffer2 = new byte[10];
        TarUtils.formatUnsignedOctalString(4621434688967111312L, testBuffer2, 0, testBuffer2.length);
        assertEquals("017777711111312", new String(testBuffer2));

        byte[] testBuffer3 = new byte[10];
        try {
            TarUtils.formatUnsignedOctalString(-1, testBuffer3, 0, testBuffer3.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] testBuffer = new byte[13];
        TarUtils.formatOctalBytes(01234567775L, testBuffer, 0, testBuffer.length);
        assertEquals("000067557767012", new String(testBuffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] testBuffer = new byte[12];
        TarUtils.formatLongOctalBytes(1353135765241789821L, testBuffer, 0, testBuffer.length);
        assertEquals("01777555551777742", new String(testBuffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] testBuffer = new byte[13];
        TarUtils.formatCheckSumOctalBytes(1129384711341156272L, testBuffer, 0, testBuffer.length);
        assertEquals("01777711044455664", new String(testBuffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testBuffer = new byte[]{0x12, 0x34, 0x56, 0x78};
        assertEquals(0x12345678L, TarUtils.computeCheckSum(testBuffer));
    }
}