import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { ' ', '2', '5', '6', ' ', '\0' };
        assertEquals(256, TarUtils.parseOctal(buffer, 0, 5));
        
        byte[] invalidBuffer = new byte[] { ' ', '9', '0', ' ', '\0' };
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        byte[] allNullBuffer = new byte[] { 0, 0, 0, 0 };
        assertEquals(0, TarUtils.parseOctal(allNullBuffer, 0, 4));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] octalBuffer = new byte[] { '0', '7', '7', ' ', '\0' };
        assertEquals(63, TarUtils.parseOctalOrBinary(octalBuffer, 0, 4));
        
        byte[] binaryBuffer = new byte[] { (byte) 0x80, 0x01, 0x00, 0x00 };
        assertEquals(128, TarUtils.parseOctalOrBinary(binaryBuffer, 0, 4));
    }

    @Test
    public void testParseBoolean() {
        byte[] trueBuffer = new byte[] { 1 };
        assertTrue(TarUtils.parseBoolean(trueBuffer, 0));
        
        byte[] falseBuffer = new byte[] { 0 };
        assertFalse(TarUtils.parseBoolean(falseBuffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[] { 'T', 'e', 's', 't', '\0' };
        assertEquals("Test", TarUtils.parseName(nameBuffer, 0, 5));
        
        byte[] longNameBuffer = new byte[] { 'L', 'o', 'n', 'g', 'N', 'a', 'm', 'e', 0 };
        assertEquals("LongName", TarUtils.parseName(longNameBuffer, 0, 9));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes("Test", buffer, 0, 5);
        assertArrayEquals(new byte[] { 'T', 'e', 's', 't', '\0' }, buffer);
        assertEquals(5, offset);

        buffer = new byte[5];
        offset = TarUtils.formatNameBytes("LongName", buffer, 0, 5);
        assertArrayEquals(new byte[] { 'L', 'o', 'n', 'g', 0 }, buffer);
        assertEquals(5, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[4];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 4);
        assertArrayEquals(new byte[] { '0', '1', '7', '?' }, buffer);
        
        byte[] invalidBuffer = new byte[2];
        try {
            TarUtils.formatUnsignedOctalString(999, invalidBuffer, 0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { 1, 2, 3, 4, 5 };
        assertEquals(15, TarUtils.computeCheckSum(buffer));
    }
}