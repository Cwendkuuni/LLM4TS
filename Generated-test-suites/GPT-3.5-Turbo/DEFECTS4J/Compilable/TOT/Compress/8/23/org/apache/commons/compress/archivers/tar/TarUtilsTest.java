package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    // Test case for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 0, 0, 0, 0, 0};
        int offset = 0;
        int length = 6;
        long expected = 0;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    // Test case for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = "test".getBytes();
        int offset = 0;
        int length = 4;
        String expected = "test";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    // Test case for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        byte[] expected = {'t', 'e', 's', 't', 0, 0};
        TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(expected, buf);
    }
    
    // Test case for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 10;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        byte[] expected = {'0', '0', '0', '1', '2', '0'};
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertArrayEquals(expected, buf);
    }

    // Test case for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 10;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        byte[] expected = {'0', '0', '0', '1', '0', ' '};
        TarUtils.formatOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
    }

    // Test case for formatLongOctalBytes method
    @Test
    public void testFormatLongOctalBytes() {
        long value = 10;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {'0', '0', '0', '1', ' '};
        TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
    }

    // Test case for formatCheckSumOctalBytes method
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 10;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        byte[] expected = {'0', '0', '0', '1', 0, ' '};
        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
    }

    // Test case for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] buf = "test".getBytes();
        long expected = 448;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}
