import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 49, 50, 51};
        int offset = 0;
        int length = 4;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(0123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{105, 110, 112, 117, 116};
        int offset = 0;
        int length = 5;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("input", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "output";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{111, 117, 116, 112, 117, 116}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(new byte[]{48, 48, 48, 48, 48, 49, 51, 55}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        long value = 1234567890123456789L;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(8, result);
        assertArrayEquals(new byte[]{48, 48, 48, 48, 48, 49, 50, 32}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(8, result);
        assertArrayEquals(new byte[]{48, 48, 48, 48, 48, 49, 51, 32}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(8, result);
        assertArrayEquals(new byte[]{48, 48, 48, 48, 48, 49, 50, 32}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[4];
        for (int i = 0; i < 4; i++) {
            buf[i] = (byte) (i + 1);
        }
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(10, result);
    }
}