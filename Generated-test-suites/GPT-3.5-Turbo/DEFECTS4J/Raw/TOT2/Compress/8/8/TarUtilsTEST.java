import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "12345".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(12345, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "TestName".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("TestName", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        int result = TarUtils.formatNameBytes("TestName", buffer, 0, 8);
        assertEquals(8, result);
        assertEquals("TestName\0\0\0", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(12345L, buffer, 0, 8);
        assertEquals("00012345", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[8];
        int result = TarUtils.formatOctalBytes(12345L, buffer, 0, 8);
        assertEquals("00012345 ", new String(buffer));
        assertEquals(8, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "Test".getBytes();
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(436, result);
    }

    // Add more test cases for the remaining methods as needed

}