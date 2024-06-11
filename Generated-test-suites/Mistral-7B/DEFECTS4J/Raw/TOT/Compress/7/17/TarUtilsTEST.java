import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '5'};
        long expected = 017775L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Incorrect octal value parsed from buffer", expected, result);

        // Edge case: empty buffer
        buffer = new byte[0];
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Parsed value from empty buffer should be 0", 0, result);

        // Error case: non-octal input
        buffer = new byte[]{(byte) 'a', (byte) '1', (byte) '0', (byte) '7'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException with non-octal input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p'};
        String expected = "tar.txt.arc.zip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Incorrect name parsed from buffer", expected, result);

        // Edge case: empty buffer
        buffer = new byte[0];
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Parsed name from empty buffer should be empty string", "", result);

        // Error case: non-name input
        buffer = new byte[]{(byte) 'a', (byte) '1', (byte) 'b', (byte) 'c'};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException with non-name input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my_file.txt";
        byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) '_', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't'};
        byte[] buf = new byte[expected.length];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals("Incorrect name format in buffer", expected, buf, 0, offset);

        // Edge case: name longer than buffer
        name = "very_long_name_that_will_not_fit_in_the_buffer";
        expected = new byte[255]; // Max length of buffer
        buf = new byte[expected.length];
        offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertEquals("Name longer than buffer should cause truncation", expected.length-1, offset);

        // Error case: name with invalid characters
        name = "my_file\0xt";
        try {
            TarUtils.formatNameBytes(name, buf, 0, buf.length);
            fail("Expected IllegalArgumentException with invalid characters in name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0xF0;
        byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '4', (byte) '0'};
        byte[] buf = new byte[expected.length];
        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        assertArrayEquals("Incorrect octal string format in buffer", expected, buf, 0, expected.length);

        // Edge case: value longer than buffer
        value = 0xFFFFF;
        buf = new byte[3];
        try {
            TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
            fail("Expected IllegalArgumentException with value longer than buffer");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0xF0;
        byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '4', (byte) '0', (byte) ' '};
        byte[] buf = new byte[expected.length];
        int offset = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals("Incorrect octal string format in buffer", expected, buf, 0, offset);

        // Edge case: value longer than buffer
        value = 0xFFFFF;
        buf = new byte[5];
        try {
            TarUtils.formatOctalBytes(value, buf, 0, buf.length);
            fail("Expected IllegalArgumentException with value longer than buffer");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x42, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x21, (byte) 0x0};
        long expected = 0x93B722B12D021523L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals("Incorrect checksum computed from buffer", expected, result);
    }
}