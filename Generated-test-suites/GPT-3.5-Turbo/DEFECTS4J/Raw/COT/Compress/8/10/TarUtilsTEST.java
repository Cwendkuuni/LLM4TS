import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{49, 50, 51};
        int offset = 0;
        int length = 3;
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{49, 50, 71};
        int offset = 0;
        int length = 3;
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{104, 101, 108, 108, 111};
        int offset = 0;
        int length = 5;
        String expected = "hello";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expectedOffset = 5;
        assertEquals(expectedOffset, TarUtils.formatNameBytes(name, buf, offset, length));
        assertArrayEquals(new byte[]{104, 101, 108, 108, 111}, buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{48, 48, 49, 50, 51}, buffer);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringTooLong() {
        long value = 123456;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expectedOffset = 5;
        assertEquals(expectedOffset, TarUtils.formatOctalBytes(value, buf, offset, length));
        assertArrayEquals(new byte[]{48, 48, 49, 50, 32}, buf);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expectedOffset = 5;
        assertEquals(expectedOffset, TarUtils.formatLongOctalBytes(value, buf, offset, length));
        assertArrayEquals(new byte[]{48, 48, 49, 50, 32}, buf);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expectedOffset = 5;
        assertEquals(expectedOffset, TarUtils.formatCheckSumOctalBytes(value, buf, offset, length));
        assertArrayEquals(new byte[]{48, 48, 49, 50, 32}, buf);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{49, 50, 51};
        long expected = 150;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }
}