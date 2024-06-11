package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(1234567890, result);

        byte[] buffer2 = {' ', ' ', '1', '0', '0', ' '};
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(100, result2);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello World", result);

        byte[] buffer2 = {'J', 'a', 'v', 'a', ' ', 'T', 'e', 's', 't', ' ', 'C', 'l', 'a', 's', 's', 0};
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertEquals("Java Test Class", result2);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Hello World";
        byte[] buf = new byte[20];
        int offset = 3;
        int length = 11;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(14, result);
        assertEquals(0, buf[0]);
        assertEquals(0, buf[1]);
        assertEquals(0, buf[2]);
        assertEquals('H', buf[3]);
        assertEquals('e', buf[4]);
        assertEquals('l', buf[5]);
        assertEquals('l', buf[6]);
        assertEquals('o', buf[7]);
        assertEquals(' ', buf[8]);
        assertEquals('W', buf[9]);
        assertEquals('o', buf[10]);
        assertEquals('r', buf[11]);
        assertEquals(0, buf[12]);
        assertEquals(0, buf[13]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[12];
        int offset = 2;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(1234567890, buf, offset, length);
        assertEquals(1234567890, Long.parseLong(new String(buf, offset, length - 2), 8));
        
        TarUtils.formatUnsignedOctalString(100, buf, offset, length);
        assertEquals(100, Long.parseLong(new String(buf, offset, length - 2), 8));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 6;

        int result = TarUtils.formatOctalBytes(1234567890, buf, offset, length);
        assertEquals(8, result);
        assertEquals('1', buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('6', buf[2]);
        assertEquals('6', buf[3]);
        assertEquals('1', buf[4]);
        assertEquals('6', buf[5]);
        assertEquals(' ', buf[6]);
        assertEquals(0, buf[7]);

        int result2 = TarUtils.formatOctalBytes(100, buf, offset, length);
        assertEquals(8, result2);
        assertEquals('1', buf[0]);
        assertEquals('4', buf[1]);
        assertEquals('4', buf[2]);
        assertEquals(' ', buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[9];
        int offset = 1;
        int length = 8;

        int result = TarUtils.formatLongOctalBytes(1234567890, buf, offset, length);
        assertEquals(9, result);
        assertEquals(0, buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals('6', buf[3]);
        assertEquals('6', buf[4]);
        assertEquals('1', buf[5]);
        assertEquals('6', buf[6]);
        assertEquals(' ', buf[7]);
        assertEquals(0, buf[8]);

        int result2 = TarUtils.formatLongOctalBytes(100, buf, offset, length);
        assertEquals(9, result2);
        assertEquals(0, buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('4', buf[2]);
        assertEquals('4', buf[3]);
        assertEquals(' ', buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
        assertEquals(0, buf[8]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 6;

        int result = TarUtils.formatCheckSumOctalBytes(1234567890, buf, offset, length);
        assertEquals(8, result);
        assertEquals('1', buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('6', buf[2]);
        assertEquals('6', buf[3]);
        assertEquals('1', buf[4]);
        assertEquals('6', buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(' ', buf[7]);

        int result2 = TarUtils.formatCheckSumOctalBytes(100, buf, offset, length);
        assertEquals(8, result2);
        assertEquals('1', buf[0]);
        assertEquals('4', buf[1]);
        assertEquals('4', buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(' ', buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(1025, result);

        byte[] buf2 = {'J', 'a', 'v', 'a', ' ', 'T', 'e', 's', 't', ' ', 'C', 'l', 'a', 's', 's', 0};
        long result2 = TarUtils.computeCheckSum(buf2);
        assertEquals(1584, result2);
    }
}