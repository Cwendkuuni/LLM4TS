import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{50, 48, 49, 55}; // "2017" in ASCII
        assertEquals(2017, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{104, 101, 108, 108, 111, 0}; // "hello" in ASCII
        assertEquals("hello", TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buf, 0, name.length());
        
        assertEquals("test", new String(buf, 0, 4));
        assertEquals(10, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        assertEquals(15, TarUtils.computeCheckSum(buffer));
    }

    // Add more test cases for other methods including edge cases and exceptions

}