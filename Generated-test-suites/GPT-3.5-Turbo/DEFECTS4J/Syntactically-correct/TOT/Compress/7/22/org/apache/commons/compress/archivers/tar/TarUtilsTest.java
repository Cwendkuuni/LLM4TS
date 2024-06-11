import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    private byte[] buffer;

    @Before
    public void setUp() {
        // Initialize the buffer with data for testing
        buffer = new byte[10];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) i;
        }
    }

    @After
    public void tearDown() {
        // Clean up the buffer
        buffer = null;
    }

    @Test
    public void testParseOctal() {
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(1357L, result);
    }

    @Test
    public void testParseName() {
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("0123456789", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "example";
        int offset = 0;
        int length = name.length();
        int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String result = new String(buffer, offset, length);
        Assert.assertEquals(name, result);
        Assert.assertEquals(offset + length, newOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567890L;
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer);
        Assert.assertEquals("4463253502", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 2468L;
        int offset = 0;
        int length = buffer.length;
        int newOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        String result = new String(buffer, offset, length);
        Assert.assertEquals("0000002468 ", result);
        Assert.assertEquals(offset + length, newOffset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 9876543210L;
        int offset = 0;
        int length = buffer.length;
        int newOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        String result = new String(buffer, offset, length);
        Assert.assertEquals("00000", result);
        Assert.assertEquals(offset + length, newOffset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 54321L;
        int offset = 0;
        int length = buffer.length;
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        String result = new String(buffer, offset, length);
        Assert.assertEquals("00000054321", result);
        Assert.assertEquals(offset + length, newOffset);
    }

    @Test
    public void testComputeCheckSum() {
        long sum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(45L, sum);
    }

}