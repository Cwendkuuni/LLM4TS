import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 55}; // octal value of 07
        int offset = 0;
        int length = 2;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(7, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{116, 101, 115, 116, 0}; // "test"
        int offset = 0;
        int length = 5;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int updatedOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(4, updatedOffset);
        assertEquals(116, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(115, buf[2]);
        assertEquals(116, buf[3]);
        assertEquals(0, buf[4]);
        for (int i = 5; i < 10; i++) {
            assertEquals(0, buf[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 15;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(55, buffer[2]);
        assertEquals(55, buffer[3]);
        assertEquals(55, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 15;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(55, buf[3]);
        assertEquals(32, buf[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 15;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(55, buf[3]);
        assertEquals(32, buf[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 15;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(55, buf[2]);
        assertEquals(55, buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{116, 101, 115, 116};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(448, result);
    }
}