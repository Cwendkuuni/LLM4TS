import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0755644L, result);

        byte[] invalidBuffer = new byte[]{(byte) 0666, (byte) 0x0a, (byte) 0, (byte) ' '}; // invalid byte
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{(byte) 0x81, (byte) 0x7f, (byte) 0, (byte) ' '};
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(29969L, result); // binary number 0x817f

        byte[] octalBuffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) ' '};
        result = TarUtils.parseOctalOrBinary(octalBuffer, 0, octalBuffer.length);
        assertEquals(0755644L, result); // octal number 0755644

        byte[] invalidBuffer = new byte[]{(byte) 0x81, (byte) 0x81, (byte) 0, (byte) ' '}; // binary number too large
        try {
            TarUtils.parseOctalOrBinary(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean result = TarUtils.parseBoolean(bufferTrue, 0);
        assertTrue(result);

        byte[] bufferFalse = new byte[]{0};
        result = TarUtils.parseBoolean(bufferFalse, 0);
        assertFalse(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{67, 65, 6E, 67, 69, 6E, 65, 0}; // test
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{67, 65, 6E, 67, 69, 6E, 65, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 54321;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0, '5', '4', '3', '2', '1', '0', '0', '0', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 54321;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0, '0', '1', '0', '1', '4', '3', '2', '1', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expectedOctal = new byte[]{0, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        assertArrayEquals(expectedOctal, buffer, 0, 11);

        byte[] bufferBinary = new byte[13];
        TarUtils.formatLongOctalOrBinaryBytes(value, bufferBinary, 0, bufferBinary.length);
        byte[] expectedBinary = new byte[]{0x81, (byte) 0x7f, 0, 0, 0, 0, 0, 0, 0, 0, 0x7f, 0x81, 0};
        assertArrayEquals(expectedBinary, bufferBinary, 0, bufferBinary.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        assertArrayEquals(expected, buffer, 0, 11);
    }

}