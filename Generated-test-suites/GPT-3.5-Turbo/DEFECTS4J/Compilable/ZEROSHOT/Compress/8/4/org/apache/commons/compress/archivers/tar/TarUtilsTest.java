package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

   @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0x31;
        buffer[1] = 0x30;
        buffer[2] = 0x30;
        buffer[3] = 0x20;
        buffer[4] = 0x20;
        buffer[5] = 0x20;
        buffer[6] = 0x20;
        buffer[7] = 0x20;
        buffer[8] = 0x20;
        buffer[9] = 0x20;
        
        long result = TarUtils.parseOctal(buffer, 0, 10);
        assertEquals(100, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[2];
        buffer[0] = 0x31;
        buffer[1] = 0x47;
        
        TarUtils.parseOctal(buffer, 0, 2);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[8];
        buffer[0] = 0x68;
        buffer[1] = 0x65;
        buffer[2] = 0x6C;
        buffer[3] = 0x6C;
        buffer[4] = 0x6F;
        buffer[5] = 0x20;
        buffer[6] = 0x20;
        buffer[7] = 0x20;
        
        String result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        String name = "world";
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(8, result);

        // check if the buffer is filled correctly
        for (int i = offset; i < offset + length; i++) {
            assertEquals(name.charAt(i - offset), (char)buf[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        long value = 100;
        
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        // check if the buffer is filled correctly
        for (int i = offset; i < offset + length; i++) {
            assertEquals((byte)(value % 10 +'0'), buf[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooBig() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        long value = 10000000000L;
        
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        long value = 100;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(8, result);

        // check if the buffer is filled correctly
        for (int i = offset; i < offset + length; i++) {
            if (i == offset + length - 2) {
                assertEquals(' ', (char)buf[i]);
            } else if (i == offset + length - 1) {
                assertEquals(0, buf[i]);
            } else {
                assertEquals((byte)(value % 10 +'0'), buf[i]);
            }
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        long value = 100;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(8, result);

        // check if the buffer is filled correctly
        for (int i = offset; i < offset + length; i++) {
            if (i == offset + length - 1) {
                assertEquals(' ', (char)buf[i]);
            } else {
                assertEquals((byte)(value % 10 +'0'), buf[i]);
            }
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        long value = 100;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(8, result);

        // check if the buffer is filled correctly
        for (int i = offset; i < offset + length; i++) {
            if (i == offset + length - 2) {
                assertEquals(0, buf[i]);
            } else if (i == offset + length - 1) {
                assertEquals(' ', (char)buf[i]);
            } else {
                assertEquals((byte)(value % 10 +'0'), buf[i]);
            }
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[8];
        buf[0] = 0x48;
        buf[1] = 0x65;
        buf[2] = 0x6C;
        buf[3] = 0x6C;
        buf[4] = 0x6F;
        buf[5] = 0x20;
        buf[6] = 0x20;
        buf[7] = 0x20;

        long expected = 0x48 + 0x65 + 0x6C + 0x6C + 0x6F + 0x20 + 0x20 + 0x20;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}