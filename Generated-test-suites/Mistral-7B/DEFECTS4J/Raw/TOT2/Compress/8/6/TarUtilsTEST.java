import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);
    }

    @Test
    public void testParseOctal_LeadingSpaces() {
        byte[] buffer = new byte[]{' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', 'a', '5', '6', '7', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Add other test cases for each method in TarUtils class

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "test.txt";
        byte[] buf = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'t', 'e', 's', 't', '.', 't', 'x', 't'}, buf, 0, offset);
    }

    @Test
    public void testFormatNameBytes_LargeName() {
        String name = "this_is_a_very_long_name_that_will_test_the_truncation_behavior.txt";
        byte[] buf = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'t', 'h', 'i', 's', '_', 'i', '_', 'a', '_', 'v', '_', 'e', '_', 'r', '_', 'y', '_', 'l', '_', 'o', '_', 'n', '_', 'g', '_', 't', 'h', 'a', 't', '_', 't', 'r', 'u', 'n', 'c', 'a', 't', 'i', 'o', 'n', '.', 't', 'x', 't'}, buf, 0, offset);
    }

    // Add other test cases for each method in TarUtils class
}