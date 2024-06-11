import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {(byte) '1', (byte) '2', (byte) '3'};
        int offset = 0;
        int length = buffer.length;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(83, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'T', 'e', 's', 't', 0};
        int offset = 0;
        int length = buffer.length;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{'T', 'e', 's', 't', 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 83;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '1', (byte) '3', (byte) '3'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 83;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '1', (byte) '3', (byte) '3'}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 83;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '1', (byte) '3', (byte) ' '}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 83;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '1', (byte) '3', 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {'T', 'e', 's', 't'};
        long expectedSum = 'T' + 'e' + 's' + 't';

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expectedSum, result);
    }
}