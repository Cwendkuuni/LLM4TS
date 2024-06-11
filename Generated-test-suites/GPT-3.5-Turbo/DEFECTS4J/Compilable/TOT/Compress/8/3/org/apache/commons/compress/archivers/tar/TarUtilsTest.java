package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // typical use case
        byte[] buffer1 = "123  ".getBytes();
        assertEquals(123L, TarUtils.parseOctal(buffer1, 0, 5));

        // edge case: buffer with all NULs
        byte[] buffer2 = "000\0".getBytes();
        assertEquals(0L, TarUtils.parseOctal(buffer2, 0, 4));

        // error scenario: invalid byte
        byte[] buffer3 = "123 H ".getBytes();
        try {
            TarUtils.parseOctal(buffer3, 0, 5);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 72 at offset 3 in '123 H ' len=5", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // typical use case
        byte[] buffer1 = "filename\0".getBytes();
        assertEquals("filename", TarUtils.parseName(buffer1, 0, 9));

        // edge case: buffer longer than name
        byte[] buffer2 = "name0000000\0".getBytes();
        assertEquals("name", TarUtils.parseName(buffer2, 0, 12));

        // edge case: name longer than buffer
        byte[] buffer3 = "longname\0".getBytes();
        assertEquals("longnam", TarUtils.parseName(buffer3, 0, 8));
    }

    @Test
    public void testFormatNameBytes() {
        // typical use case
        String name1 = "filename";
        byte[] buf1 = new byte[8];
        TarUtils.formatNameBytes(name1, buf1, 0, 8);
        assertArrayEquals("filename".getBytes(), buf1);

        // edge case: name longer than buffer
        String name2 = "longname";
        byte[] buf2 = new byte[5];
        TarUtils.formatNameBytes(name2, buf2, 0, 5);
        assertArrayEquals("longn".getBytes(), buf2);

        // edge case: buffer longer than name
        String name3 = "name";
        byte[] buf3 = new byte[10];
        TarUtils.formatNameBytes(name3, buf3, 0, 10);
        assertArrayEquals("name\0\0\0\0\0".getBytes(), buf3);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // typical use case
        byte[] buf1 = new byte[5];
        TarUtils.formatUnsignedOctalString(123L, buf1, 0, 5);
        assertArrayEquals("00123".getBytes(), buf1);

        // edge case: value is 0
        byte[] buf2 = new byte[3];
        TarUtils.formatUnsignedOctalString(0L, buf2, 0, 3);
        assertArrayEquals("000".getBytes(), buf2);
        
        // error scenario: value cannot fit in buffer
        byte[] buf3 = new byte[2];
        try {
            TarUtils.formatUnsignedOctalString(1000L, buf3, 0, 2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("1000=1750 will not fit in octal number buffer of length 2", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // typical use case
        byte[] buf1 = new byte[6];
        TarUtils.formatOctalBytes(123L, buf1, 0, 6);
        assertArrayEquals("000123 ".getBytes(), buf1);

        // edge case: value is 0
        byte[] buf2 = new byte[5];
        TarUtils.formatOctalBytes(0L, buf2, 0, 5);
        assertArrayEquals("00000 ".getBytes(), buf2);
        
        // error scenario: value cannot fit in buffer
        byte[] buf3 = new byte[2];
        try {
            TarUtils.formatOctalBytes(1000L, buf3, 0, 2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("1000=1750 will not fit in octal number buffer of length 2", e.getMessage());
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        // typical use case
        byte[] buf1 = new byte[6];
        TarUtils.formatLongOctalBytes(123L, buf1, 0, 6);
        assertArrayEquals("000123 ".getBytes(), buf1);

        // edge case: value is 0
        byte[] buf2 = new byte[5];
        TarUtils.formatLongOctalBytes(0L, buf2, 0, 5);
        assertArrayEquals("00000 ".getBytes(), buf2);
        
        // error scenario: value cannot fit in buffer
        byte[] buf3 = new byte[2];
        try {
            TarUtils.formatLongOctalBytes(1000L, buf3, 0, 2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("1000=1750 will not fit in octal number buffer of length 2", e.getMessage());
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // typical use case
        byte[] buf1 = new byte[6];
        TarUtils.formatCheckSumOctalBytes(123L, buf1, 0, 6);
        assertArrayEquals("000123 ".getBytes(), buf1);

        // edge case: value is 0
        byte[] buf2 = new byte[5];
        TarUtils.formatCheckSumOctalBytes(0L, buf2, 0, 5);
        assertArrayEquals("00000 ".getBytes(), buf2);
        
        // error scenario: value cannot fit in buffer
        byte[] buf3 = new byte[2];
        try {
            TarUtils.formatCheckSumOctalBytes(1000L, buf3, 0, 2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("1000=1750 will not fit in octal number buffer of length 2", e.getMessage());
        }
    }

    @Test
    public void testComputeCheckSum() {
        // typical use case
        byte[] buf1 = "abcdefg".getBytes();
        assertEquals(1031L, TarUtils.computeCheckSum(buf1));

        // edge case: buffer with all NULs
        byte[] buf2 = new byte[10];
        assertEquals(0L, TarUtils.computeCheckSum(buf2));

        // error scenario: buffer with negative bytes
        byte[] buf3 = new byte[] {-1, -1, -1, -1};
        assertEquals(85898815L, TarUtils.computeCheckSum(buf3));
    }
}