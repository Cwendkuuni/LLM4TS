import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int length = 7;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(0123456L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '8'};
        int offset = 0;
        int length = 4;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', 't', '.', 'c', 'h', 'e', 'c', 'k', 's', 'u', 'm', '.'};
        int offset = 0;
        int length = 15;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("tar.achtums.checksum.", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.achtums.checksum.";
        byte[] buf = new byte[15];
        int offset = 0;
        int length = 15;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(new byte[]{'t', 'a', 'r', '.', 'a', 't', '.', 'c', 'h', 'e', 'c', 'k', 's', 'u', '.'}, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123456;
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '0', ' '}, buf);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 123456;
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', '0', ' ', '0'}, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buf = new byte[]{'t', 'a', 'r', '.', 'a', 't', '.', 'c', 'h', 'e', 'c', 'k', 's', 'u', 'm', '.'};

        long result = TarUtils.computeCheckSum(buf);

        long expected = 0x53686174612E617463686573686D61737361;
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_TrailingNull() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '0'};
        int offset = 0;
        int length = 8;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(0123456L, result);
    }

    @Test
    public void testParseOctal_TrailingSpace() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' '};
        int offset = 0;
        int length = 8;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctal_InvalidLength() {
        byte[] buffer = new byte[]{'0'};
        int offset = 0;

        try {
            TarUtils.parseOctal(buffer, offset, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName_TrailingNull() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', 't', '.', 'c', 'h', 'e', 'c', 'k', 's', 'u', 'm', '\u0000'};
        int offset = 0;
        int length = 16;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("tar.achtums.checksum.", result);
    }

    @Test
    public void testParseName_TrailingSpace() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', 't', '.', 'c', 'h', 'e', 'c', 'k', 's', 'u', 'm', ' '};
        int offset = 0;
        int length = 16;

        try {
            TarUtils.parseName(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes_InvalidLength() {
        String name = "tar.achtums.checksum.";
        byte[] buf = new byte[13];
        int offset = 0;

        try {
            TarUtils.formatNameBytes(name, buf, offset, 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar.achtums.checksum.longerthanbuffer.";
        byte[] buf = new byte[11];
        int offset = 0;

        try {
            TarUtils.formatNameBytes(name, buf, offset, 11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString_ValueLongerThanBuffer() {
        long value = 12345678901234567890L;
        byte[] buf = new byte[15];
        int offset = 0;
        int length = 15;

        try {
            TarUtils.formatUnsignedOctalString(value, buf, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes_ValueLongerThanBuffer() {
        long value = 12345678901234567890L;
        byte[] buf = new byte[19];
        int offset = 0;
        int length = 19;

        try {
            TarUtils.formatOctalBytes(value, buf, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValueLongerThanBuffer() {
        long value = 12345678901234567890L;
        byte[] buf = new byte[15];
        int offset = 0;
        int length = 15;

        try {
            TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}