import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) ' '};
        long expected = 0x755L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 064, (byte) ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testParseOctalMissingTrailingSpaceOrNUL() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 0644};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalAllNUL() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{(byte) 0, (byte) 0644, (byte) ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 0x81, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07};
        long expected = 0x8101020304050607L;
        long result = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(expected, result);

        byte[] buffer2 = new byte[]{(byte) 0755, (byte) 0644, (byte) ' '};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(expected, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{(byte) 0x81, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x0F, (byte) 0x05, (byte) 0x06, (byte) 0x07};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{
            0x73, 0x61, 0x73, 0x73, 0x65, 0x73, 0x73, 0x2e,
            0x69, 0x6f, 0x6e, 0x65, 0x6e, 0x74, 0x2e, 0x63,
            0x61, 0x6d, 0x65, 0x00
        };
        String expected = "myfile.name.csh";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.name.csh";
        byte[] buffer = new byte[TarConstants.MAXNAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, TarConstants.MAXNAMELEN);
        assertEquals(offset, TarConstants.MAXNAMELEN);

        String result = new String(buffer, 0, TarConstants.MAXNAMELEN);
        assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 017777777777L;
        byte[] buffer = new byte[15];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        // Check the first character is '0'
        assertEquals('0', buffer[0]);

        // Check the last 8 characters are '7'
        for (int i = buffer.length - 8; i < buffer.length; i++) {
            assertEquals('7', buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 017777777777L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);

        String result = new String(buffer, 0, buffer.length - 1); // exclude the trailing space
        assertEquals("07777777777", result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x1234567890ABCDEF0L;
        byte[] buffer = new byte[15];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);

        // The first character should be '0'
        assertEquals('0', buffer[0]);

        // The last 8 characters should be the binary number (hexadecimal representation)
        for (int i = buffer.length - 8; i < buffer.length; i++) {
            int index = (buffer.length - i - 1) / 2;
            assertEquals(Long.toString(0xFF & (value >>> (8 * index))).charAt(0), buffer[i]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0x75, 0x73, 0x61, 0x73, 0x73, 0x65, 0x73, 0x73, 0x2e, 0x69, 0x6f, 0x6e, 0x65, 0x6e, 0x74, 0x2e, 0x63, 0x61, 0x6d, 0x65, 0x00, 0x00};
        long checksum = TarUtils.computeCheckSum(buf);
        long expectedChecksum = 0x4a7f4c8938314832L;
        assertEquals(expectedChecksum, checksum);
    }
}