package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '3', '5', '4', (byte) ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(1354, result);

        // Edge case - all NULs
        byte[] allNuls = {0, 0, 0, 0, 0};
        long resultAllNuls = TarUtils.parseOctal(allNuls, 0, allNuls.length);
        assertEquals(0, resultAllNuls);

        // Error scenario - missing trailing space/NUL
        byte[] missingTrailingSpace = {'1', '2', '3'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(missingTrailingSpace, 0, missingTrailingSpace.length);
        });
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = {'t', 'e', 's', 't', 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);

        // Edge case - buffer length is reached
        byte[] reachedBufferLength = {'a', 'b', 'c'};
        String resultReachedBufferLength = TarUtils.parseName(reachedBufferLength, 0, reachedBufferLength.length);
        assertEquals("abc", resultReachedBufferLength);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name = "test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, updatedOffset);
        assertEquals("test", new String(buf));

        // Edge case - name longer than buffer
        String longName = "thisisaverylongname";
        byte[] bufLongName = new byte[5];
        int updatedOffsetLongName = TarUtils.formatNameBytes(longName, bufLongName, 0, 5);
        assertEquals(5, updatedOffsetLongName);
        assertEquals("thisi", new String(bufLongName));
    }

    // Include more test cases for other methods

}