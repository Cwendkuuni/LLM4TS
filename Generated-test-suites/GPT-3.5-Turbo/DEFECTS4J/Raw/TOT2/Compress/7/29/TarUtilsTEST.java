import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = {'1', '2', '3'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(83, result);
    }

    @Test
    public void testParseNameValidInput() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "example";
        byte[] buffer = new byte[10];
        int newOffset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, newOffset);
        assertEquals('e', buffer[0]);
        assertEquals('x', buffer[1]);
        assertEquals('a', buffer[2]);
        assertEquals('m', buffer[3]);
        assertEquals('p', buffer[4]);
        assertEquals('l', buffer[5]);
        assertEquals('e', buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    // Add more test cases for other methods

}