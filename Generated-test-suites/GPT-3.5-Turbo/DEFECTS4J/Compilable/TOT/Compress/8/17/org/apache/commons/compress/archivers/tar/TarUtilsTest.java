package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_typicalCase() {
        byte[] buffer = {49, 50, 55, 0};
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(127, result);
    }

    @Test
    public void testParseOctal_emptyBuffer() {
        byte[] buffer = {};
        long result = TarUtils.parseOctal(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidByte() {
        byte[] buffer = {49, 50, 55, 56};
        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName_typicalCase() {
        byte[] buffer = {65, 66, 67, 68, 0};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("ABCD", result);
    }

    @Test
    public void testParseName_emptyBuffer() {
        byte[] buffer = {};
        String result = TarUtils.parseName(buffer, 0, 0);
        assertEquals("", result);
    }

    @Test
    public void testParseName_nullTerminatorMissing() {
        byte[] buffer = {65, 66, 67, 68};
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("ABCD", result);
    }

    @Test
    public void testFormatNameBytes_typicalCase() {
        String name = "ABCD";
        byte[] buf = new byte[4];
        int result = TarUtils.formatNameBytes(name, buf, 0, 4);
        assertEquals(4, result);
        assertEquals(65, buf[0]);
        assertEquals(66, buf[1]);
        assertEquals(67, buf[2]);
        assertEquals(68, buf[3]);
    }

    @Test
    public void testFormatNameBytes_nameLongerThanBuffer() {
        String name = "ABCDE";
        byte[] buf = new byte[4];
        int result = TarUtils.formatNameBytes(name, buf, 0, 4);
        assertEquals(4, result);
        assertEquals(65, buf[0]);
        assertEquals(66, buf[1]);
        assertEquals(67, buf[2]);
        assertEquals(68, buf[3]);
    }

    @Test
    public void testFormatUnsignedOctalString_typicalCase() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(127, buf, 0, 5);
        assertEquals(49, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooLarge() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(1000, buf, 0, 5);
    }

    @Test
    public void testFormatOctalBytes_typicalCase() {
        byte[] buf = new byte[6];
        int result = TarUtils.formatOctalBytes(127, buf, 0, 6);
        assertEquals(6, result);
        assertEquals(49, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(32, buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_valueTooLarge() {
        byte[] buf = new byte[6];
        TarUtils.formatOctalBytes(1000, buf, 0, 6);
    }

    @Test
    public void testFormatLongOctalBytes_typicalCase() {
        byte[] buf = new byte[5];
        int result = TarUtils.formatLongOctalBytes(127, buf, 0, 5);
        assertEquals(5, result);
        assertEquals(49, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(32, buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes_typicalCase() {
        byte[] buf = new byte[6];
        int result = TarUtils.formatCheckSumOctalBytes(127, buf, 0, 6);
        assertEquals(6, result);
        assertEquals(49, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(32, buf[4]);
        assertEquals(0, buf[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_valueTooLarge() {
        byte[] buf = new byte[6];
        TarUtils.formatCheckSumOctalBytes(1000, buf, 0, 6);
    }

    @Test
    public void testComputeCheckSum_typicalCase() {
        byte[] buf = {65, 66, 67, 68, 0};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(271, result);
    }

}