import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[6];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        buffer[3] = '4';
        buffer[4] = '5';
        buffer[5] = 0;

        long result = TarUtils.parseOctal(buffer, 0, 6);

        assertEquals(12345, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = 'T';
        buffer[1] = 'e';
        buffer[2] = 's';
        buffer[3] = 't';
        buffer[4] = 0;

        String result = TarUtils.parseName(buffer, 0, 5);

        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Example";
        byte[] buf = new byte[8];
        int offset = 0;

        int newOffset = TarUtils.formatNameBytes(name, buf, offset, 8);

        assertEquals(8, newOffset);
        assertEquals('E', buf[0]);
        assertEquals('x', buf[1]);
        assertEquals('a', buf[2]);
        assertEquals('m', buf[3]);
        assertEquals('p', buf[4]);
        assertEquals('l', buf[5]);
        assertEquals('e', buf[6]);
        assertEquals(0, buf[7]);
    }

    // Add more test cases for other methods as needed...

}