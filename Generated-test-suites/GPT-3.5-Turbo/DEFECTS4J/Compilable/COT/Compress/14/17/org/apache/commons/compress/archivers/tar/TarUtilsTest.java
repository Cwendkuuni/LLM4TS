package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = 1;
        buffer[2] = '0';
        buffer[3] = '7';
        buffer[4] = ' ';
        buffer[5] = '\0';

        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(7, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[10];
        buffer[0] = 'a';
        buffer[1] = 'b';
        buffer[2] = 'c';
        buffer[3] = 'd';
        buffer[4] = ' ';
        buffer[5] = '\0';

        TarUtils.parseOctal(buffer, 0, 6);
    }

    @Test
    public void testParseOctalWithLeadingNull() {
        byte[] buffer = new byte[10];
        buffer[0] = '\0';
        buffer[1] = '1';
        buffer[2] = '0';
        buffer[3] = '2';
        buffer[4] = ' ';
        buffer[5] = '\0';

        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(0, result);
    }

    // More test cases can be added for other methods in the TarUtils class
}

