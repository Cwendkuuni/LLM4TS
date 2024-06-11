import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01777770L, result);

        buffer = new byte[]{'0', '1', '4', '0', '7', '5', '5', '1', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(014075510L, result);

        buffer = new byte[]{'0'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'1'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'7', '8'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'.' , '.' , 'h', 'e', 'l', 'l', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello.tar.gz", result);

        buffer = new byte[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", result);

        buffer = new byte[]{'.'};
        String expectedErrorMessage = "Invalid byte 42 at offset 0 in '' len=0";
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException with message: " + expectedErrorMessage);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }

        buffer = new byte[]{'h', 'e', 'l', 'l', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', '.', '.', '.', '.', '.'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello.tar...", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello.tar.gz";
        byte[] buffer = new byte[256];

        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length(), result);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }

        result = TarUtils.formatNameBytes("", buffer, 0, buffer.length);
        assertEquals(0, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(0, result);

        value = 1;
        result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(1, result);
        assertEquals("0", new String(buffer, 0, result));

        value = 17;
        result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(3, result);
        assertEquals("002", new String(buffer, 0, result));

        value = 255;
        result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(3, result);
        assertEquals("0377", new String(buffer, 0, result));

        value = 12345;
        result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(7, result);
        assertEquals("012625", new String(buffer, 0, result));

        value = 1234567;
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("000000 ", new String(buffer, 0, result));

        value = 1;
        result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("000001 ", new String(buffer, 0, result));

        value = 17;
        result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("002002 ", new String(buffer, 0, result));

        value = 255;
        result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("037700 ", new String(buffer, 0, result));

        value = 12345;
        result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(13, result);
        assertEquals("012625  ", new String(buffer, 0, result));

        value = 1234567;
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("0000000 ", new String(buffer, 0, result));

        value = 1;
        result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("0000001 ", new String(buffer, 0, result));

        value = 17;
        result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("0000021 ", new String(buffer, 0, result));

        value = 255;
        result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals("0377000 ", new String(buffer, 0, result));

        value = 12345;
        result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, result);
        assertEquals("0126250 ", new String(buffer, 0, result));

        value = 1234567;
        try {
            TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(8, result);
        assertEquals("0000000  ", new String(buffer, 0, result));

        value = 1;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(8, result);
        assertEquals("0000001  ", new String(buffer, 0, result));

        value = 17;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(8, result);
        assertEquals("0000021  ", new String(buffer, 0, result));

        value = 255;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(8, result);
        assertEquals("0377001  ", new String(buffer, 0, result));

        value = 12345;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, result);
        assertEquals("0126251  ", new String(buffer, 0, result));

        value = 1234567;
        try {
            TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        long checksum = TarUtils.computeChecksum(buffer);
        long calculatedChecksum = 0;

        for (byte b : buffer) {
            calculatedChecksum += TarUtils.BYTE_MASK & b;
        }

        assertEquals(checksum, calculatedChecksum);

        buffer = new byte[0];
        checksum = TarUtils.computeChecksum(buffer);
        assertEquals(0, checksum);

        buffer = new byte[]{(byte) 0x80};
        checksum = TarUtils.computeChecksum(buffer);
        calculatedChecksum = 0;
        calculatedChecksum += TarUtils.BYTE_MASK & buffer[0];
        assertEquals(checksum, calculatedChecksum);

        buffer = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        checksum = TarUtils.computeChecksum(buffer);
        calculatedChecksum = 0;
        for (byte b : buffer) {
            calculatedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, calculatedChecksum);
    }
}