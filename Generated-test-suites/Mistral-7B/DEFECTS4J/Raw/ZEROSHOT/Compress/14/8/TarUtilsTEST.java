import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', ' ', '0'};
        long octalValue = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(07554L, octalValue);

        byte[] bufferInvalid = new byte[]{'0', '7', '5', '5', '4', 'a'};
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferTrailingNul = new byte[]{'0', '7', '5', '5', '4'};
        try {
            TarUtils.parseOctal(bufferTrailingNul, 0, bufferTrailingNul.length);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferLeadingNul = new byte[]{0, '7', '5', '5', '4', ' ', '0'};
        long octalValueLeadingNul = TarUtils.parseOctal(bufferLeadingNul, 0, bufferLeadingNul.length);
        assertEquals(0L, octalValueLeadingNul);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '7', '5', '5', '4', ' ', '0'};
        long octalValue = TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length);
        assertEquals(07554L, octalValue);

        byte[] bufferBinary = new byte[]{(byte) 0x81, 0x31, 0x32, 0x33, 0x34, ' ', 0};
        long binaryValue = TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
        assertEquals(5674272048L, binaryValue);

        byte[] bufferInvalid = new byte[]{'0', '7', '5', '5', '4', 'a'};
        try {
            TarUtils.parseOctalOrBinary(bufferInvalid, 0, bufferInvalid.length);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferTrailingNul = new byte[]{'0', '7', '5', '5', '4'};
        try {
            TarUtils.parseOctalOrBinary(bufferTrailingNul, 0, bufferTrailingNul.length);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferLeadingNul = new byte[]{0, '7', '5', '5', '4', ' ', 0};
        long octalValueLeadingNul = TarUtils.parseOctalOrBinary(bufferLeadingNul, 0, bufferLeadingNul.length);
        assertEquals(0L, octalValueLeadingNul);
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean booleanValue = TarUtils.parseBoolean(bufferTrue, 0);
        assertTrue(booleanValue);

        byte[] bufferFalse = new byte[]{0};
        boolean booleanValueFalse = TarUtils.parseBoolean(bufferFalse, 0);
        assertFalse(booleanValueFalse);

        byte[] bufferInvalid = new byte[]{'a'};
        try {
            TarUtils.parseBoolean(bufferInvalid, 0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] bufferName = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
        String name = TarUtils.parseName(bufferName, 0, bufferName.length);
        assertEquals("abcde", name);

        byte[] bufferNameTrailingNul = new byte[]{'a', 'b', 'c', 'd', 'e'};
        String nameTrailingNul = TarUtils.parseName(bufferNameTrailingNul, 0, bufferNameTrailingNul.length);
        assertEquals("abcde", nameTrailingNul);

        byte[] bufferNameInvalid = new byte[]{'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            TarUtils.parseName(bufferNameInvalid, 0, bufferNameInvalid.length);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcde";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(TarConstants.NAME_LENGTH, length);
        for (int i = 0; i < length; ++i) {
            assertEquals(name.charAt(i), (char) buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01234567755";
        for (int i = 0; i < buffer.length; ++i) {
            assertEquals(expected.charAt(i), (char) buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567755  ";
        for (int i = 0; i < buffer.length; ++i) {
            assertEquals(expected.charAt(i), (char) buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        if (value <= TarConstants.MAXID) {
            String expected = "01234567755";
            for (int i = 0; i < buffer.length; ++i) {
                assertEquals(expected.charAt(i), (char) buffer[i]);
            }
        } else {
            byte expectedByte = (byte) (value >>> 56);
            byte bufferByte = buffer[0];
            assertEquals(expectedByte, bufferByte);
            assertEquals(value & 0xFFFFFFFFFFFFL, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length - 1) & 0xFFFFFFFFFFFFL);
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[11];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567755  ";
        for (int i = 0; i < buffer.length - 1; ++i) {
            assertEquals(expected.charAt(i), (char) buffer[i]);
        }
        assertEquals(0, buffer[buffer.length - 1]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f'};
        long checksum = TarUtils.computeCheckSum(buffer);
        byte[] expected = new byte[]{0x22, 0xd6, 0x3f, 0x14, 0x18, 0x48};
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], buffer[i + 1]);
        }
        assertEquals(checksum, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }
}