import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0x30, 0x31, 0x32, 0x33}; // "0123"
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(83, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Testing";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 7;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(7, newOffset);
        assertEquals((byte) 'T', buf[0]);
        assertEquals((byte) 'e', buf[1]);
        assertEquals((byte) 's', buf[2]);
        assertEquals((byte) 't', buf[3]);
        assertEquals((byte) 'i', buf[4]);
        assertEquals((byte) 'n', buf[5]);
        assertEquals((byte) 'g', buf[6]);
        assertEquals((byte) 0, buf[7]);
        assertEquals((byte) 0, buf[8]);
        assertEquals((byte) 0, buf[9]);
    }

    // Add more test methods for other public methods in TarUtils

}