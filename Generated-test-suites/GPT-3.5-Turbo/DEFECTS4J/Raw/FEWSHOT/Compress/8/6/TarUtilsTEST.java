package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    private byte[] buffer;

    @Before
    public void setup() {
        buffer = new byte[100];
    }

    @Test
    public void testParseOctal() {
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, 2));
        assertEquals(7L, TarUtils.parseOctal(buffer, 0, 2));
        assertEquals(123456L, TarUtils.parseOctal(buffer, 0, 6));

        try {
            TarUtils.parseOctal(buffer, 0, 1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            TarUtils.parseOctal(buffer, 0, 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testParseName() {
        buffer[0] = 'f';
        buffer[1] = 'o';
        buffer[2] = 'o';
        buffer[3] = 0;

        assertEquals("foo", TarUtils.parseName(buffer, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo";
        int offset = 0;
        int length = 4;

        TarUtils.formatNameBytes(name, buffer, offset, length);

        assertTrue(buffer[0] == 'f');
        assertTrue(buffer[1] == 'o');
        assertTrue(buffer[2] == 'o');
        assertTrue(buffer[3] == 0);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 6);

        assertEquals("123456", new String(buffer, 0, 6));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        int offset = 0;
        int length = 8;

        TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertTrue(buffer[0] == '1');
        assertTrue(buffer[1] == '2');
        assertTrue(buffer[2] == '3');
        assertTrue(buffer[3] == '4');
        assertTrue(buffer[4] == '5');
        assertTrue(buffer[5] == '6');
        assertTrue(buffer[6] == ' ');
        assertTrue(buffer[7] == 0);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        int offset = 0;
        int length = 7;

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertTrue(buffer[0] == ' ');
        assertTrue(buffer[1] == '1');
        assertTrue(buffer[2] == '2');
        assertTrue(buffer[3] == '3');
        assertTrue(buffer[4] == '4');
        assertTrue(buffer[5] == '5');
        assertTrue(buffer[6] == '6');
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456;
        int offset = 0;
        int length = 8;

        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertTrue(buffer[0] == '1');
        assertTrue(buffer[1] == '2');
        assertTrue(buffer[2] == '3');
        assertTrue(buffer[3] == '4');
        assertTrue(buffer[4] == '5');
        assertTrue(buffer[5] == '6');
        assertTrue(buffer[6] == 0);
        assertTrue(buffer[7] == ' ');
    }

    @Test
    public void testComputeCheckSum() {
        buffer[0] = 1;
        buffer[1] = 2;
        buffer[2] = 3;
        buffer[3] = 4;
        buffer[4] = 5;

        assertEquals(15L, TarUtils.computeCheckSum(buffer));
    }
}