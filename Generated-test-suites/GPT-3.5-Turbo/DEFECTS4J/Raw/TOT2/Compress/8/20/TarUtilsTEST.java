import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50};
        assertEquals(123, TarUtils.parseOctal(buffer, 0, 3));

        byte[] buffer2 = {48, 50, 49};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> TarUtils.parseOctal(buffer2, 0, 3));
        assertEquals("Invalid byte 50 at offset 1 in '020' len=3", exception.getMessage());
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0, 0};
        assertEquals("abc", TarUtils.parseName(buffer, 0, 5));

        byte[] buffer2 = {97, 98, 99, 100};
        assertEquals("abcd", TarUtils.parseName(buffer2, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("abc", buf, 0, 5);
        byte[] expected = {97, 98, 99, 0, 0};
        assertArrayEquals(expected, buf);

        offset = TarUtils.formatNameBytes("abcd", buf, 0, 5);
        byte[] expected2 = {97, 98, 99, 100, 0};
        assertArrayEquals(expected2, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {97, 98, 99, 100};
        assertEquals(394, TarUtils.computeCheckSum(buffer));

        byte[] buffer2 = {97, 98, 99, 100, 101};
        assertEquals(495, TarUtils.computeCheckSum(buffer2));
    }

    // Add more test cases for other methods as needed

}