import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    private TarUtils tarUtils;

    @Before
    public void setUp() {
        tarUtils = new TarUtils();
    }

    @Test
    public void testParseOctal_ValidInput_ReturnsExpectedNumber() {
        byte[] buffer = "456\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 456L;

        long result = tarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_LeadingSpaces_IgnoreLeadingSpaces() {
        byte[] buffer = "   789\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 789L;

        long result = tarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_TrailingSpace_StopParsing() {
        byte[] buffer = "123  \0".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 123L;

        long result = tarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_NonOctalInput_ThrowIllegalArgumentException() {
        byte[] buffer = "abc\0".getBytes();
        int offset = 0;
        int length = buffer.length;

        try {
            tarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 0 in 'abc'", e.getMessage());
        }
    }

    @Test
    public void testParseName_ValidInput_ReturnsExpectedName() {
        byte[] buffer = "Hello\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        String expected = "Hello";

        String result = tarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_TrailingNull_StopParsing() {
        byte[] buffer = "Hi\0There".getBytes();
        int offset = 0;
        int length = 2;
        String expected = "Hi";

        String result = tarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_ShortName_OnlyCopyRequiredBytes() {
        String name = "Short";
        byte[] buf = new byte[name.length() + 1];
        int offset = 0;
        int length = buf.length;
        byte[] expected = "Short\0".getBytes();

        int result = tarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatNameBytes_LongName_TruncateOutput() {
        String name = "ThisIsALongName";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        byte[] expected = "ThisIsALon".getBytes();

        int result = tarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString_ZeroValue_ReturnsExpectedString() {
        long value = 0L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "000\0".getBytes();

        tarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString_PositiveValue_ReturnsExpectedString() {
        long value = 123L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "123\0".getBytes();

        tarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueTooLarge_ThrowsIllegalArgumentException() {
        long value = 99999L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        tarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_ValidValue_ReturnsExpectedBuffer() {
        long value = 123L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "123 \0".getBytes();

        int result = tarUtils.formatOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatLongOctalBytes_ValidValue_ReturnsExpectedBuffer() {
        long value = 123456L;
        byte[] buffer = new byte[7];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "123456 \0".getBytes();

        int result = tarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidValue_ReturnsExpectedBuffer() {
        long value = 123456L;
        byte[] buffer = new byte[7];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "123456\0 ".getBytes();

        int result = tarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer_ReturnsExpectedSum() {
        byte[] buffer = "Test".getBytes();
        long expected = 448L;

        long result = tarUtils.computeCheckSum(buffer);

        assertEquals(expected, result);
    }

}