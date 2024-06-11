import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This class provides test cases for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '1', '6', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4'};
        long expected = 011644444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with valid binary number.
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{0x41, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        long expected = 1023L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with valid octal string.
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '1', '6', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4'};
        long expected = 011644444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test parseBoolean method with true value.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        assertTrue(expected == actual);
    }

    /**
     * Test parseBoolean method with false value.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        assertFalse(expected == actual);
    }

    /**
     * Test parseName method with valid name.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'f', 'i', 'l', 'e', 0};
        String expected = "tar entry file";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test formatNameBytes method
     */
    @Test
    public void testFormatNameBytes() {
        String name = "tar entry file";
        byte[] expected = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'f', 'i', 'l', 'e', 0, ' '};
        byte[] buf = new byte[expected.length];
        int offset = TarUtils.formatNameBytes(name, buf, 0, expected.length);
        byte[] actual = new byte[offset];
        System.arraycopy(buf, 0, actual, 0, offset);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test formatUnsignedOctalString method with valid value.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 01164444444444444444444444444444444444444444444444444444444444444L;
        byte[] expected = new byte[]{'0', '1', '1', '6', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Test formatNameBytes method with invalid length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalidLength() {
        String name = "tar entry file";
        byte[] buf = new byte[name.length() - 1];
        TarUtils.formatNameBytes(name, buf, 0, buf.length);
    }

    /**
     * Test formatOctalBytes method with valid value.
     */
    @Test
    public void testFormatOctalBytesValid() {
        long value = 01164444444444444444444444444444444444444444444444444444444444444L;
        byte[] expected = new byte[]{'0', '1', '1', '6', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4'};
        byte[] buffer = new byte[expected.length + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] actual = new byte[expected.length];
        System.arraycopy(buffer, 0, actual, 0, expected.length);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid value as octal.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidOctal() {
        long value = 01164444444444444444444444444444444444444444444444444444444444444L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] actual = new byte[TarConstants.UIDLEN];
        System.arraycopy(buffer, 0, actual, 0, TarConstants.UIDLEN);
        assertArrayEquals(new byte[]{'0', '1', '1', '6', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4'}, actual);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid value as binary.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidBinary() {
        long value = 1023L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] actual = new byte[TarConstants.UIDLEN];
        System.arraycopy(buffer, 0, actual, 0, TarConstants.UIDLEN);
        assertArrayEquals(new byte[]{0x41, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, actual);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesInvalidValue() {
        long value = 1024L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }
}