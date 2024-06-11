import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50, 51};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = {48, 57, 55, 52}; // invalid digit '9'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        assertEquals(3, offset);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[4];
        TarUtils.formatUnsignedOctalString(123L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{49, 55, 51, 0}, buffer);

        buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(123L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{55, 51, 0}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidLength() {
        byte[] buffer = new byte[2];
        TarUtils.formatUnsignedOctalString(123L, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(123L, buffer, 0, buffer.length);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 55, 51, 32, 0}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatLongOctalBytes(123L, buffer, 0, buffer.length);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 55, 51, 32, 0}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(123L, buffer, 0, buffer.length);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 55, 51, 0, 32}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {65, 66, 67};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(195, result);
    }

}