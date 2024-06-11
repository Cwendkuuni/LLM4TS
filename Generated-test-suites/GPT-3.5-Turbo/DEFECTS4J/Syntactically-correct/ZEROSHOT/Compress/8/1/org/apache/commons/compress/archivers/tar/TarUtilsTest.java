import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[4];
        buffer[0] = 48;
        buffer[1] = 55;
        buffer[2] = 54;
        buffer[3] = 0;

        assertEquals(76, TarUtils.parseOctal(buffer, 0, 4));

        buffer[0] = 48;
        buffer[1] = 55;
        buffer[2] = 56;
        buffer[3] = 32;

        assertEquals(78, TarUtils.parseOctal(buffer, 0, 4));

        buffer[0] = 49;
        buffer[1] = 48;
        buffer[2] = 32;
        buffer[3] = 0;

        assertEquals(80, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[1];
        buffer[0] = 50;

        TarUtils.parseOctal(buffer, 0, 1);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = 97;
        buffer[1] = 98;
        buffer[2] = 99;
        buffer[3] = 100;
        buffer[4] = 0;

        assertEquals("abcd", TarUtils.parseName(buffer, 0, 5));

        buffer[0] = 65;
        buffer[1] = 66;
        buffer[2] = 67;
        buffer[3] = 68;
        buffer[4] = 0;

        assertEquals("ABCD", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcd";
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        TarUtils.formatNameBytes(name, buffer, 0, 8);

        assertEquals(97, buffer[0]);
        assertEquals(98, buffer[1]);
        assertEquals(99, buffer[2]);
        assertEquals(100, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 10;
        byte[] buffer = new byte[3];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);

        assertEquals(48, buffer[0]);
        assertEquals(49, buffer[1]);
        assertEquals(0, buffer[2]);

        value = 100;
        buffer = new byte[4];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 4);

        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(49, buffer[2]);
        assertEquals(48, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidValue() {
        long value = 10000;
        byte[] buffer = new byte[3];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 100;
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;

        TarUtils.formatOctalBytes(value, buffer, 0, 5);

        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(49, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(32, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 100;
        byte[] buffer = new byte[4];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;

        TarUtils.formatLongOctalBytes(value, buffer, 0, 4);

        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(49, buffer[2]);
        assertEquals(32, buffer[3]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 100;
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;

        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 5);

        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(49, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(32, buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[4];
        buffer[0] = 48;
        buffer[1] = 55;
        buffer[2] = 54;
        buffer[3] = 0;

        assertEquals(151, TarUtils.computeCheckSum(buffer));

        buffer[0] = 49;
        buffer[1] = 48;
        buffer[2] = 32;
        buffer[3] = 0;

        assertEquals(58, TarUtils.computeCheckSum(buffer));
    }

}