package org.apache.commons.compress.archivers.tar; // Make sure the test package matches the class package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Test cases for TarUtils class.
 */
public class TarUtilsTest {

    // 

    @Test
    public void testParseOctal_ValidOctalString() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01755770L, result);
    }

    @Test
    public void testParseOctal_EmptyInput() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '8'}; // Invalid byte '8'
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctal_TrailingNULIsMissing() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7'}; // Missing trailing NUL
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctal_LeadingSpaces() {
        byte[] buffer = new byte[]{' ', '0', '7', '5', '5', '7', '7', '7', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01755770L, result);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = new byte[]{0x20, (byte) 'T', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'F', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(" Tar.gzip", result);
    }

    @Test
    public void testParseName_EmptyInput() {
        byte[] buffer = new byte[0];
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", result);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "Tar.gzip";
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(11, offset);

        for (int i = 0; i < 10; ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }

        assertEquals(0, buffer[10]);
    }

    @Test
    public void testFormatNameBytes_LongName() {
        String name = "ThisIsATestNameThatIsLongerThanTheBuffer";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);

        for (int i = 0; i < 10; ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }

        for (int i = 10; i < name.length(); ++i) {
            assertEquals(0, buffer[i]);
        }
    }

    @Test
    public void testFormatNameBytes_EmptyName() {
        String name = "";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);

        for (int i = 0; i < 10; ++i) {
            assertEquals(0, buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(8, offset);

        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '1', buffer[1]);
        assertEquals((byte) '2', buffer[2]);
        assertEquals((byte) '3', buffer[3]);
        assertEquals((byte) '4', buffer[4]);
        assertEquals((byte) '5', buffer[5]);
        assertEquals((byte) ' ', buffer[6]);
        assertEquals(0, buffer[7]);
    }

    @Test
    public void testFormatUnsignedOctalString_LongValue() {
        long value = Long.parseUnsignedLong("1234567890123456789012345678901234567890", 8);
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(20, offset);

        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '1', buffer[1]);
        // ...
        assertEquals((byte) '8', buffer[18]);
        assertEquals((byte) '9', buffer[19]);
        assertEquals(0, buffer[20]);
    }

    @Test
    public void testFormatOctalBytes_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);

        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '1', buffer[1]);
        assertEquals((byte) '2', buffer[2]);
        assertEquals((byte) '3', buffer[3]);
        assertEquals((byte) '4', buffer[4]);
        assertEquals((byte) '5', buffer[5]);
        assertEquals((byte) ' ', buffer[6]);
        assertEquals((byte) '0', buffer[7]);
        assertEquals((byte) '5', buffer[8]);
        assertEquals((byte) '7', buffer[9]);
        assertEquals((byte) '7', buffer[10]);
        assertEquals((byte) '0', buffer[11]);
    }

    @Test
    public void testFormatLongOctalBytes_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(11, offset);

        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '1', buffer[1]);
        assertEquals((byte) '2', buffer[2]);
        assertEquals((byte) '3', buffer[3]);
        assertEquals((byte) '4', buffer[4]);
        assertEquals((byte) '5', buffer[5]);
        assertEquals((byte) ' ', buffer[6]);
        assertEquals((byte) '0', buffer[7]);
        assertEquals((byte) '5', buffer[8]);
        assertEquals((byte) '7', buffer[9]);
        assertEquals(0, buffer[10]);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);

        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '1', buffer[1]);
        assertEquals((byte) '2', buffer[2]);
        assertEquals((byte) '3', buffer[3]);
        assertEquals((byte) '4', buffer[4]);
        assertEquals((byte) '5', buffer[5]);
        assertEquals((byte) ' ', buffer[6]);
        assertEquals((byte) '0', buffer[7]);
        assertEquals((byte) '5', buffer[8]);
        assertEquals((byte) '7', buffer[9]);
        assertEquals(0, buffer[10]);
        assertEquals(0, buffer[11]);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer() {
        byte[] buffer = new byte[]{(byte) 0x67, (byte) 0x65, (byte) 0x64, (byte) 0x53, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x61, (byte) 0x6E, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x2E, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x6D, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x2E, (byte) 0x70, (byte) 0x6C, (byte) 0x65, (byte) 0x2F, (byte) 0x69, (byte) 0x6F, (byte) 0x2E, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x69, (byte) 0x6E, (byte) 0x67, (byte) 0x3B};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }

    // 
}