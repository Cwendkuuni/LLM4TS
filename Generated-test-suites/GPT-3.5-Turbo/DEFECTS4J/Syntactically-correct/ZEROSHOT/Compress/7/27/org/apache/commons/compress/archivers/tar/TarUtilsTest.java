import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) '0';
        buffer[1] = (byte) '1';
        buffer[2] = (byte) '2';
        buffer[3] = (byte) '3';
        buffer[4] = (byte) '4';
        buffer[5] = (byte) '5';
        buffer[6] = (byte) '6';
        buffer[7] = (byte) '7';
        buffer[8] = 0;
        buffer[9] = (byte) ' ';

        long result = TarUtils.parseOctal(buffer, 0, 9);

        assertEquals(01234567, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) 'T';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 's';
        buffer[3] = (byte) 't';
        buffer[4] = (byte) ' ';
        buffer[5] = 0;
        buffer[6] = (byte) ' ';
        buffer[7] = (byte) ' ';
        buffer[8] = (byte) ' ';
        buffer[9] = (byte) ' ';

        String result = TarUtils.parseName(buffer, 0, 9);

        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        String name = "Test";

        int offset = TarUtils.formatNameBytes(name, buffer, 0, 9);

        assertEquals(9, offset);

        assertEquals((byte) 'T', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 's', buffer[2]);
        assertEquals((byte) 't', buffer[3]);
        assertEquals((byte) 0, buffer[4]);
        assertEquals((byte) 0, buffer[5]);
        assertEquals((byte) 0, buffer[6]);
        assertEquals((byte) 0, buffer[7]);
        assertEquals((byte) 0, buffer[8]);
        assertEquals((byte) 0, buffer[9]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[4];

        TarUtils.formatUnsignedOctalString(123, buffer, 0, 3);

        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) 0, buffer[3]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];

        int offset = TarUtils.formatOctalBytes(123, buffer, 0, 3);

        assertEquals(3, offset);

        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) ' ', buffer[3]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[3];
        int offset = TarUtils.formatLongOctalBytes(12, buffer, 0, 2);

        assertEquals(2, offset);
        assertEquals((byte) 1, buffer[0]);
        assertEquals((byte) '2', buffer[1]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(1234, buffer, 0, 4);

        assertEquals(4, offset);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) ' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[3];
        buffer[0] = (byte) 'A';
        buffer[1] = (byte) 'B';
        buffer[2] = (byte) 'C';

        long sum = TarUtils.computeCheckSum(buffer);

        assertEquals(195, sum);
    }
}