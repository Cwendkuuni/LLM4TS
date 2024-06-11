package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51}; // "123"
        int offset = 0;
        int length = buffer.length;
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {104, 101, 108, 108, 111, 0}; // "hello"
        int offset = 0;
        int length = buffer.length;
        String expected = "hello";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        int expected = offset + length;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, result);
        String resultName = new String(buf).trim();
        assertEquals(name, resultName);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer).trim();
        assertEquals(Long.toOctalString(value), result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        int expected = offset + length;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        String resultStr = new String(buf).trim();
        assertEquals(Long.toOctalString(value) + " ", resultStr);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        int expected = offset + length;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        String resultStr = new String(buf).trim();
        assertEquals(Long.toOctalString(value) + " ", resultStr);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        int expected = offset + length;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        String resultStr = new String(buf).trim();
        assertEquals(Long.toOctalString(value) + "\u0000 ", resultStr);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 50, 51}; // "123"
        long expected = 150;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}