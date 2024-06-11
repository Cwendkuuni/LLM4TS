package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * TarUtils Test Case.
 */
public class TarUtilsTest {

    private static final byte[] TEST_BUFFER = new byte[]{'p', 'r', 'o', 'j', 'e', 'c', 't', ' ', '7', '5', '5', ' ', '1', '2', '3', '4', '5', ' ', '0'};

    @Test
    public void testParseOctal() {
        long result = TarUtils.parseOctal(TEST_BUFFER, 0, TEST_BUFFER.length);
        assertEquals(0x1234557, result);
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] invalidByteBuffer = new byte[]{'p', 'r', 'o', 'j', 'e', 'c', 't', ' ', '7', '5', '5', ' ', '1', '2', '3', '4', '5', ' ', 'x'}; // invalid byte x
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidByteBuffer, 0, invalidByteBuffer.length));
    }

    @Test
    public void testParseOctalWithMissingPadding() {
        byte[] missingPaddingBuffer = new byte[]{'p', 'r', 'o', 'j', 'e', 'c', 't', ' '}; // missing trailing space
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(missingPaddingBuffer, 0, missingPaddingBuffer.length));
    }

    @Test
    public void testParseOctalWithAllNuls() {
        byte[] allNulsBuffer = new byte[0];
        long result = TarUtils.parseOctal(allNulsBuffer, 0, allNulsBuffer.length);
        assertEquals(0, result);
    }

    @Test
    public void testParseName() {
        String name = TarUtils.parseName(TEST_BUFFER, 0, TEST_BUFFER.length);
        assertEquals("project.tar", name);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "project.tar";
        byte[] resultBuffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, resultBuffer, 0, resultBuffer.length);
        assertEquals("project.tar\0\0\0\0\0\0\0\0\0\0", new String(resultBuffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x1234557;
        byte[] resultBuffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, resultBuffer, 0, resultBuffer.length);
        assertEquals("7551234", new String(resultBuffer, 0, 6));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x1234557;
        byte[] resultBuffer = new byte[10];
        TarUtils.formatOctalBytes(value, resultBuffer, 0, resultBuffer.length);
        assertEquals("7551234 \0", new String(resultBuffer, 0, 7));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testBuffer = TEST_BUFFER;
        long checkSum = TarUtils.computeCheckSum(testBuffer);
        long computedCheckSum = 0;
        for (int i = 0; i < testBuffer.length; ++i) {
            computedCheckSum += TarUtils.BYTE_MASK & testBuffer[i];
        }
        assertEquals(checkSum, computedCheckSum);
    }
}