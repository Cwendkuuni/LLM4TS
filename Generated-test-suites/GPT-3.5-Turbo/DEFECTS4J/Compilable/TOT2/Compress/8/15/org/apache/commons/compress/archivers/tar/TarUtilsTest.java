package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 0, 0, 0, 0};
        assertEquals(0, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = {' ', ' ', '0', '2', 0};
        assertEquals(2, TarUtils.parseOctal(buffer2, 0, 5));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, offset);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'t', 'e', 's', 't'};
        assertEquals(448, TarUtils.computeCheckSum(buffer));
    }

    // Add more test cases for other methods...

}