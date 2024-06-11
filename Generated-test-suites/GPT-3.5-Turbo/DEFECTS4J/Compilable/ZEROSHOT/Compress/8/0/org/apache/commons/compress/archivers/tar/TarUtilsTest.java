package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validBytes_expectResult() {
        byte[] buffer = {49, 50, 51, 52};
        int offset = 0;
        int length = 4;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(1234, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidBytes_expectIllegalArgumentException() {
        byte[] buffer = {49, 50, 51, 52, 53};
        int offset = 0;
        int length = 5;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_validBytes_expectResult() {
        byte[] buffer = {97, 98, 99, 100};
        int offset = 0;
        int length = 4;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("abcd", result);
    }

    @Test
    public void testParseName_missingNullBytes_expectResult() {
        byte[] buffer = {97, 98, 99};
        int offset = 0;
        int length = 3;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes_shortName_expectResult() {
        String name = "abc";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, result);
        assertEquals(97, buf[0]);
        assertEquals(98, buf[1]);
        assertEquals(99, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatNameBytes_longName_expectResult() {
        String name = "abcdefghijk";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, result);
        assertEquals(97, buf[0]);
        assertEquals(98, buf[1]);
        assertEquals(99, buf[2]);
        assertEquals(100, buf[3]);
        assertEquals(101, buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString_positiveValue_expectResult() {
        long value = 1234;
        byte[] buffer = new byte[6];
        int offset = 1;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(48, buffer[0]);
        assertEquals(49, buffer[1]);
        assertEquals(50, buffer[2]);
        assertEquals(51, buffer[3]);
        assertEquals(52, buffer[4]);
        assertEquals(0, buffer[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_negativeValue_expectIllegalArgumentException() {
        long value = -1234;
        byte[] buffer = new byte[6];
        int offset = 1;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_positiveValue_expectResult() {
        long value = 1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(6, result);
        assertEquals(51, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(49, buf[2]);
        assertEquals(52, buf[3]);
        assertEquals(32, buf[4]);
        assertEquals(0, buf[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_negativeValue_expectIllegalArgumentException() {
        long value = -1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_positiveValue_expectResult() {
        long value = 1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(6, result);
        assertEquals(52, buf[0]);
        assertEquals(51, buf[1]);
        assertEquals(50, buf[2]);
        assertEquals(49, buf[3]);
        assertEquals(32, buf[4]);
        assertEquals(0, buf[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_negativeValue_expectIllegalArgumentException() {
        long value = -1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_positiveValue_expectResult() {
        long value = 1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(6, result);
        assertEquals(52, buf[0]);
        assertEquals(51, buf[1]);
        assertEquals(50, buf[2]);
        assertEquals(49, buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(32, buf[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_negativeValue_expectIllegalArgumentException() {
        long value = -1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum_validBuffer_expectResult() {
        byte[] buf = {49, 50, 51, 52};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(10, result);
    }

    @Test
    public void testComputeCheckSum_emptyBuffer_expectResult() {
        byte[] buf = {};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(0, result);
    }
}