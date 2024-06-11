package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0,0,0,0,0,0,0,0};
        long result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(0, result);
        
        buffer = new byte[]{1,1,1,1,1,1,1,1};
        result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(342391, result);
        
        buffer = new byte[]{4,3,2,1,0,0,0,0};
        result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(1234, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{65, 66, 67, 0, 0, 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("ABC", result);

        buffer = new byte[]{72, 65, 78, 0, 0, 0, 0, 0};
        result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("HAN", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        String name = "FOOBAR";
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'F', 'O', 'O', 'B', 'A', 'R', 0, 0}, buf);

        name = "HELLO";
        result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'H', 'E', 'L', 'L', 'O', 0, 0, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        
        long value = 0;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertArrayEquals(new byte[]{'0', '0', '0', '0'}, buf);
        
        value = 123;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertArrayEquals(new byte[]{'0', '1', '7', '3'}, buf);
        
        value = 900;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertArrayEquals(new byte[]{'3', '4', '4', '4'}, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        long value = 0;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{' ', 0, 0, 0, 0, 0, 0, 0}, buf);
        
        value = 123;
        result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'1', '7', '3', ' ', 0, 0, 0, 0}, buf);
        
        value = 999999;
        result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'4', '7', '7', '7', '7', '7', ' ', 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        long value = 0;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{' ', '0', '0', '0', '0', '0', 0, 0}, buf);
        
        value = 123;
        result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{' ', '1', '7', '3', ' ', '0', 0, 0}, buf);
        
        value = 999999;
        result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{' ', '4', '7', '7', '7', '7', '7', 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        long value = 0;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, ' ', 0, 0}, buf);
        
        value = 123;
        result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'1', '7', '3', ' ', 0, 0, 0, 0}, buf);
        
        value = 999999;
        result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(8, result);
        assertArrayEquals(new byte[]{'4', '7', '7', '7', '7', '7', 0, ' '}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{65, 66, 67, 0};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(198, result);

        buf = new byte[]{72, 65, 78, 0};
        result = TarUtils.computeCheckSum(buf);
        assertEquals(195, result);
    }

}