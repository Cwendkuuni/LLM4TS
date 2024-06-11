import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        int offset = 0;
        int length = buffer.length;

        long expectedResult = 027550L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_InvalidOctalDigit() {
        byte[] buffer = new byte[]{'0', '7', 'x', '5', '0'};
        int offset = 0;
        int length = buffer.length;

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));
    }

    @Test
    public void testParseOctal_TrailingNull() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0'};
        int offset = 0;
        int length = buffer.length;

        long expectedResult = 027550L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_TrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' '};
        int offset = 0;
        int length = buffer.length;

        long expectedResult = 027550L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_LengthReached() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        int offset = 0;
        int length = 4;

        long expectedResult = 0L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'.' , 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        int offset = 0;
        int length = buffer.length;

        String expectedResult = "tar.gzip";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName_NullInBuffer() {
        byte[] buffer = new byte[]{'0'};
        int offset = 0;
        int length = buffer.length;

        String expectedResult = "";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName_TrailingNull() {
        byte[] buffer = new byte[]{'.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '0'};
        int offset = 0;
        int length = buffer.length;

        String expectedResult = "tar.gzip";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.tar.gz";
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 12;

        int expectedResult = TarUtils.formatNameBytes(name, buf, offset, length);
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expectedResult, result);
        assertNotNull(result);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "myfile.tar.gz.long";
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 12;

        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buf, offset, length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 27550L;
        byte[] buffer = new byte[9];
        int offset = 0;
        int length = 9;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expectedString = "027550";
        String resultString = new String(buffer, 0, length);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueTooBigForBuffer() {
        long value = 1234567890L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 27550L;
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatOctalBytes_ValueTooBigForBuffer() {
        long value = 1234567890L;
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;

        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buf, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', 'w', 'W', 'o', 'r', 'l', 'd'};
        long expectedCheckSum = 1085783409212432505L;
        long checkSum = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedCheckSum, checkSum);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1085783409212432505L;
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 13;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(offset + length, result);
    }
}