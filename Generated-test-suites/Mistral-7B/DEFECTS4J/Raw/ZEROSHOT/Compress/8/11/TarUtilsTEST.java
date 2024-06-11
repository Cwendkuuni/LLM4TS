import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.TAR_MAGIC_NUMBER;
import static org.apache.commons.compress.archivers.tar.TarConstants.TAR_MAGIC_NUMBER_LEN;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0x0, (byte) '0', (byte) '7', (byte) '7', (byte) '7', 0};
        long expected = 01777L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);

        byte[] invalidBuffer = new byte[]{0x0, (byte) '8'}; // invalid byte
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] missingTrailBuffer = new byte[]{0x0, (byte) '0', (byte) '7', (byte) '7'}; // missing trailing space or NUL
        try {
            TarUtils.parseOctal(missingTrailBuffer, 0, missingTrailBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0x0, (byte) 'a', (byte) 'b', (byte) 'c', 0x0};
        String expected = "abc";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);

        byte[] missingTrailBuffer = new byte[]{0x0, (byte) 'a', (byte) 'b', (byte) 'c'}; // missing trailing space or NUL
        try {
            TarUtils.parseName(missingTrailBuffer, 0, missingTrailBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        String result = new String(buffer, 0, offset);
        assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, offset);
        assertEquals("012345", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, offset - 2); // exclude space and trailing null
        assertEquals("012345 ", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, offset - 1); // exclude trailing space
        assertEquals("01234571265 ", result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, offset - 2); // exclude NUL and trailing space
        assertEquals("01234571265 ", result);

        long checksum = TarUtils.computeCheckSum(buffer, 0, offset);
        assertEquals(value, checksum);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{TAR_MAGIC_NUMBER};
        long checksum = TarUtils.computeCheckSum(buffer, 0, buffer.length);
        long expected = TarUtils.computeCheckSum(TAR_MAGIC_NUMBER);
        assertEquals(expected, checksum);
    }
}