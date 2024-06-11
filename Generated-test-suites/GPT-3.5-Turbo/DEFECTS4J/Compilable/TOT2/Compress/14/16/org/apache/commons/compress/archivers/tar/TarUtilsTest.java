package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51, 52}; // Corresponds to ASCII values of '1', '2', '3', '4'
        long expected = 1234;
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = {127, -92}; // Corresponds to binary value of 0111 1111 1001 0100
        byte[] buffer2 = {49, 50, 51}; // Corresponds to ASCII values of '1', '2', '3'
        long expected1 = -108;
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, 2);
        assertEquals(expected1, result1);

        long expected2 = 123;
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, 3);
        assertEquals(expected2, result2);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = {1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0}; // Corresponds to ASCII values of 'a', 'b', 'c', NUL
        String expected = "abc";
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "xyz";
        byte[] buffer = new byte[5]; // Buffer longer than name
        TarUtils.formatNameBytes(name, buffer, 0, 5);
        assertEquals("x", String.valueOf((char) buffer[0]));
        assertEquals("y", String.valueOf((char) buffer[1]));
        assertEquals("z", String.valueOf((char) buffer[2]));
        assertEquals(0, buffer[3]); // Expecting NUL
        assertEquals(0, buffer[4]); // Expecting NUL
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4};
        long expected = 10; // Sum of 1 + 2 + 3 + 4
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

}