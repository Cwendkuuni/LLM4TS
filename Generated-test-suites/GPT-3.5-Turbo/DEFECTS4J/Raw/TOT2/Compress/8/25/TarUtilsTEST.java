import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = " 123 ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(83, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "TestFile\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("TestFile", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TestName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(9, offset);
        assertEquals("TestName\0\0\0\0", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, buffer.length);
        assertEquals("0000000173", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(123, buffer, 0, buffer.length);
        assertEquals(9, offset);
        assertEquals("00000000123", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "TestCheckSum".getBytes();
        long result = TarUtils.computeCheckSum(buffer);
        // Sum of ASCII values for 'T', 'e', 's', 't', 'C', 'h', 'e', 'c', 'k', 'S', 'u', 'm'
        assertEquals(1056, result);
    }

}