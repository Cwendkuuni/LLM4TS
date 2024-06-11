import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '0';
        buffer[2] = '0';
        buffer[3] = '0';
        buffer[4] = '\0';

        long expected = 1000;
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[10];
        buffer[0] = 'a';
        buffer[1] = 'b';

        TarUtils.parseOctal(buffer, 0, 2);
    }

    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[10];
        buffer[0] = 't';
        buffer[1] = 'e';
        buffer[2] = 's';
        buffer[3] = 't';
        buffer[4] = '\0';

        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseNameInvalid() {
        byte[] buffer = new byte[10];
        buffer[0] = 't';
        buffer[1] = 'e';
        buffer[2] = 's';
        buffer[3] = 't';

        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytesValid() {
        byte[] buf = new byte[10];
        String name = "test";

        int expected = 10;
        int result = TarUtils.formatNameBytes(name, buf, 0, 10);
        assertEquals(expected, result);

        String expectedString = "test\0\0\0\0\0";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testFormatNameBytesInvalid() {
        byte[] buf = new byte[5];
        String name = "testlongerthanbuffer";

        int expected = 5;
        int result = TarUtils.formatNameBytes(name, buf, 0, 5);
        assertEquals(expected, result);

        String expectedString = "testl";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testFormatUnsignedOctalStringValid() {
        byte[] buf = new byte[10];
        long value = 1000;

        TarUtils.formatUnsignedOctalString(value, buf, 0, 10);

        String expectedString = "0000001750";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalid() {
        byte[] buf = new byte[5];
        long value = 100000;

        TarUtils.formatUnsignedOctalString(value, buf, 0, 5);
    }

    @Test
    public void testFormatOctalBytesValid() {
        byte[] buf = new byte[10];
        long value = 1000;

        int expected = 10;
        int result = TarUtils.formatOctalBytes(value, buf, 0, 10);
        assertEquals(expected, result);

        String expectedString = "0000001750 ";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalid() {
        byte[] buf = new byte[5];
        long value = 100000;

        TarUtils.formatOctalBytes(value, buf, 0, 5);
    }

    @Test
    public void testFormatLongOctalBytesValid() {
        byte[] buf = new byte[10];
        long value = 1000;

        int expected = 10;
        int result = TarUtils.formatLongOctalBytes(value, buf, 0, 10);
        assertEquals(expected, result);

        String expectedString = "000000175 ";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesInvalid() {
        byte[] buf = new byte[5];
        long value = 100000;

        TarUtils.formatLongOctalBytes(value, buf, 0, 5);
    }

    @Test
    public void testFormatCheckSumOctalBytesValid() {
        byte[] buf = new byte[10];
        long value = 1000;

        int expected = 10;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, 0, 10);
        assertEquals(expected, result);

        String expectedString = "000000175 \0";
        String resultString = new String(buf);
        assertEquals(expectedString, resultString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesInvalid() {
        byte[] buf = new byte[5];
        long value = 100000;

        TarUtils.formatCheckSumOctalBytes(value, buf, 0, 5);
    }

    @Test
    public void testComputeCheckSumValid() {
        byte[] buf = new byte[5];
        buf[0] = 't';
        buf[1] = 'e';
        buf[2] = 's';
        buf[3] = 't';

        long expected = 448;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSumEmpty() {
        byte[] buf = {};

        long expected = 0;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSumValidNegative() {
        byte[] buf = new byte[5];
        buf[0] = 't';
        buf[1] = 'e';
        buf[2] = 's';
        buf[3] = 't';

        long expected = -208;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}