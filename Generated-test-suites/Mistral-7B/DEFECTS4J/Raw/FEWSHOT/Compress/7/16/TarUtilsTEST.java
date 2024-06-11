package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{(byte) '7', (byte) '5', (byte) '4', (byte) '3', (byte) '2', (byte) '1'};
        assertEquals(01754321L, TarUtils.parseOctal(testData, 0, testData.length));

        byte[] testDataTrailingNull = new byte[]{(byte) '7', (byte) '5', (byte) '4', (byte) '3', (byte) '2', (byte) '1', (byte) 0};
        assertEquals(01754321L, TarUtils.parseOctal(testDataTrailingNull, 0, testDataTrailingNull.length));

        byte[] testDataTrailingSpace = new byte[]{(byte) '7', (byte) '5', (byte) '4', (byte) '3', (byte) '2', (byte) '1', (byte) ' '};
        assertEquals(01754321L, TarUtils.parseOctal(testDataTrailingSpace, 0, testDataTrailingSpace.length));

        byte[] testDataInvalidFirstDigit = new byte[]{(byte) '9', (byte) '5', (byte) '4', (byte) '3', (byte) '2', (byte) '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testDataInvalidFirstDigit, 0, testDataInvalidFirstDigit.length));

        byte[] testDataInvalidLength = new byte[]{(byte) '7', (byte) '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testDataInvalidLength, 0, testDataInvalidLength.length));
    }

    @Test
    public void testParseName() {
        byte[] testData = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) 0};
        assertEquals("tar.archives.checksums", TarUtils.parseName(testData, 0, testData.length));

        byte[] testDataTrailingNull = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) 0, (byte) 0};
        assertEquals("tar.archives.checksums", TarUtils.parseName(testDataTrailingNull, 0, testDataTrailingNull.length));

        byte[] testDataTrailingSpace = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) 0, (byte) ' '};
        assertEquals("tar.archives.checksums", TarUtils.parseName(testDataTrailingSpace, 0, testDataTrailingSpace.length));

        byte[] testDataInvalidLength = new byte[]{(byte) 't', (byte) 'a', (byte) 'r'};
        assertNull(TarUtils.parseName(testDataInvalidLength, 0, testDataInvalidLength.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.archives.checksums";
        byte[] buf = new byte[32];

        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(name.getBytes(), buf, 0, name.length());
        assertEquals(offset, buf.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buf = new byte[10];

        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '7', (byte) '4', (byte) '5', (byte) '0', (byte) 0}, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[12];

        int offset = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '7', (byte) '4', (byte) '5', (byte) ' ', (byte) 0, (byte) 0}, buf);
        assertEquals(offset, buf.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[11];

        int offset = TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '7', (byte) '4', (byte) '5', (byte) ' ', (byte) 0}, buf);
        assertEquals(offset, buf.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '7', (byte) '4', (byte) '5', (byte) ' ', (byte) 0, (byte) 0, (byte) ' '}, buf);
        assertEquals(offset, buf.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        long expectedChecksum = 0x123456789abcdef1L;
        long computedChecksum = TarUtils.computeChecksum(buf);

        assertEquals(expectedChecksum, computedChecksum);
    }
}