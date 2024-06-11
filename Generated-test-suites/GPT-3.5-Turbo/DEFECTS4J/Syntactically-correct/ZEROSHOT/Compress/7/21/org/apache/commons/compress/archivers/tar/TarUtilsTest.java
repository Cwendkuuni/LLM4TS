import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    private byte[] buffer;
    private TarUtils tarUtils;

    @Before
    public void setUp() {
        buffer = new byte[10];
        tarUtils = new TarUtils();
    }

    @Test
    public void testParseOctal() {
        buffer[0] = 49;
        buffer[1] = 48;
        buffer[2] = 48;
        long result = tarUtils.parseOctal(buffer, 0, 3);
        assertEquals(100, result);
    }

    @Test
    public void testParseName() {
        buffer[0] = 104;
        buffer[1] = 101;
        buffer[2] = 108;
        buffer[3] = 108;
        buffer[4] = 111;
        String result = tarUtils.parseName(buffer, 0, 5);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        int result = tarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, result);
        assertEquals(116, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(115, buffer[2]);
        assertEquals(116, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        tarUtils.formatUnsignedOctalString(100, buffer, 0, 10);
        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(48, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(48, buffer[4]);
        assertEquals(48, buffer[5]);
        assertEquals(48, buffer[6]);
        assertEquals(49, buffer[7]);
        assertEquals(48, buffer[8]);
        assertEquals(48, buffer[9]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        tarUtils.formatUnsignedOctalString(12345, buffer, 0, 5);
    }

    @Test
    public void testFormatOctalBytes() {
        int result = tarUtils.formatOctalBytes(100, buffer, 0, 10);
        assertEquals(10, result);
        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(48, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(48, buffer[4]);
        assertEquals(48, buffer[5]);
        assertEquals(48, buffer[6]);
        assertEquals(49, buffer[7]);
        assertEquals(32, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        int result = tarUtils.formatLongOctalBytes(100, buffer, 0, 10);
        assertEquals(10, result);
        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(48, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(48, buffer[4]);
        assertEquals(48, buffer[5]);
        assertEquals(48, buffer[6]);
        assertEquals(49, buffer[7]);
        assertEquals(32, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        int result = tarUtils.formatCheckSumOctalBytes(100, buffer, 0, 10);
        assertEquals(10, result);
        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(48, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(48, buffer[4]);
        assertEquals(48, buffer[5]);
        assertEquals(48, buffer[6]);
        assertEquals(49, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(32, buffer[9]);
    }

    @Test
    public void testComputeCheckSum() {
        buffer[0] = 49;
        buffer[1] = 48;
        buffer[2] = 48;
        long result = tarUtils.computeCheckSum(buffer);
        assertEquals(300, result);
    }
}