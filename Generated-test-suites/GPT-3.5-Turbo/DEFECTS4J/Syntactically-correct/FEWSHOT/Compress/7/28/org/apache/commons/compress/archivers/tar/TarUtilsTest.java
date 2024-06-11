
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51}; // ascii representation of "123"
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = {48, 48, 49}; // ascii representation of "001"
        assertEquals(1L, TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {104, 101, 108, 108, 111}; // ascii representation of "hello"
        assertEquals("hello", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer2 = {119, 111, 114, 108, 100}; // ascii representation of "world"
        assertEquals("world", TarUtils.parseName(buffer2, 0, buffer2.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, name.length());

        byte[] expected = {116, 101, 115, 116, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = {48, 51, 54, 51, 0, 0, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[8];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = {48, 51, 54, 51, 32, 0, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[8];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = {48, 51, 54, 51, 32, 32, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[8];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = {48, 51, 54, 51, 0, 32, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {49, 50, 51}; // ascii representation of "123"
        assertEquals(369L, TarUtils.computeCheckSum(buffer));

        byte[] buffer2 = {48, 48, 49}; // ascii representation of "001"
        assertEquals(49L, TarUtils.computeCheckSum(buffer2));
    }

}
