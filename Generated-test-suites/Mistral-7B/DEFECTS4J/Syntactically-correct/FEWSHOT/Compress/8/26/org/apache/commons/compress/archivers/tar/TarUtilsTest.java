package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '4', '4', '3', '1', '0', '0'};
        assertEquals(0100544310L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[]{'0', '0', '7', '5', '4', 'A', '3', '1', '0', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length));

        byte[] bufferMissingTrailing = new byte[]{'0', '0', '7', '5', '4', '4', '3', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferMissingTrailing, 0, bufferMissingTrailing.length));

        byte[] bufferEmpty = new byte[0];
        assertEquals(0L, TarUtils.parseOctal(bufferEmpty, 0, bufferEmpty.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'.' , '.', 'a', '.', 'c', 'h', 'e', 'r', '.', 'm', 'y', '.', 'j', 'a', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '\0'};
        assertEquals("myjar.gz", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferEmpty = new byte[0];
        assertNull(TarUtils.parseName(bufferEmpty, 0, bufferEmpty.length));

        byte[] bufferInvalid = new byte[]{'.' , '.', 'a', '.', 'c', 'h', 'e', 'r', '.', 'm', 'y', '.', 'j', 'a', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', 'x'};
        assertNull(TarUtils.parseName(bufferInvalid, 0, bufferInvalid.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myjar.gz";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("myjar.gz", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[8];
        long value = 12345678L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '2', '3', '4', '5', '6', '7' }, buffer);

        value = 65535;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '7', '7', '7' }, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[11];
        long value = 12345678L;
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '2', '3', '4', '5', '6', '7', ' ', '0' }, buffer);

        value = 65535;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '7', '7', '7', ' ', '0' }, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 12345678L;
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '2', '3', '4', '5', '6', '7', ' ', '0' }, buffer);

        value = 65535;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '7', '7' }, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[11];
        long value = 12345678L;
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '2', '3', '4', '5', '6', '7', '0', ' ', '0' }, buffer);

        value = 65535;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{ '0', '1', '7', '7', '0', ' ', '0' }, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        long expectedChecksum = 9446331164L;
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedChecksum, checksum);
    }

    @Test
    public void testFormatNameBytesWithLongName() {
        String name = "mylongfilenamewithmorethan255characters.ext";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("mylongfilenamewithmorethan255characters.ext", new String(buffer, 0, offset));

        // Should be truncated
        byte[] bufferTruncated = new byte[10];
        offset = TarUtils.formatNameBytes(name, bufferTruncated, 0, bufferTruncated.length);
        assertEquals("mylongfilenam", new String(bufferTruncated, 0, offset));
    }

}