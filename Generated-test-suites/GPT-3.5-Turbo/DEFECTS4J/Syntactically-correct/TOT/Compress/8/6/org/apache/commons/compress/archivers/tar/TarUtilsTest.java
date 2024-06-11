import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validBuffer_offsetZero_lengthTwo() {
        byte[] buffer = new byte[]{49, 48};
        int offset = 0;
        int length = 2;

        long expected = 8;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_invalidBuffer_offsetZero_lengthTwo() {
        byte[] buffer = new byte[]{57, 99};
        int offset = 0;
        int length = 2;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 99 at offset 1 in '9c' len=2", e.getMessage());
        }
    }

    @Test
    public void testParseName_validBuffer_offsetZero_lengthFive() {
        byte[] buffer = new byte[]{116, 101, 115, 116, 0};
        int offset = 0;
        int length = 5;

        String expected = "test";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_emptyBuffer_offsetZero_lengthFive() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0};
        int offset = 0;
        int length = 5;

        String expected = "";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_validName_offsetZero_lengthFour() {
        String name = "test";
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        byte[] expected = new byte[]{116, 101, 115, 116};

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatNameBytes_longName_offsetZero_lengthFour() {
        String name = "testing";
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        byte[] expected = new byte[]{116, 101, 115, 116};

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString_valueZero_offsetZero_lengthFour() {
        long value = 0;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        byte[] expected = new byte[]{48, 48, 48, 48};

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatUnsignedOctalString_valueFive_offsetZero_lengthThree() {
        long value = 5;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        byte[] expected = new byte[]{53, 48, 48};

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatOctalBytes_valueZero_offsetZero_lengthThree() {
        long value = 0;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        byte[] expected = new byte[]{48, 48, 0};

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatOctalBytes_longValue_offsetZero_lengthThree() {
        long value = 12345;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        byte[] expected = new byte[]{50, 57, 0};

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatLongOctalBytes_valueZero_offsetZero_lengthThree() {
        long value = 0;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        byte[] expected = new byte[]{48, 0, 0};

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_valueZero_offsetZero_lengthThree() {
        long value = 0;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        byte[] expected = new byte[]{48, 0, 32};

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testComputeCheckSum_validBuffer() {
        byte[] buf = new byte[]{49, 50, 51, 52};

        long expected = 10;

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum_emptyBuffer() {
        byte[] buf = new byte[0];

        long expected = 0;

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expected, result);
    }
}