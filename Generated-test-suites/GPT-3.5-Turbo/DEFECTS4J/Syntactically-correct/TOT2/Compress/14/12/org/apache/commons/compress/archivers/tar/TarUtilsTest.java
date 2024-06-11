import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0x30, 0x31, 0x32, 0x33}; // "0123"
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(83, result);
    }

    @Test
    public void testParseOctalWithLeadingSpaces() {
        byte[] buffer = {0x20, 0x20, 0x30, 0x31}; // " 01"
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(1, result);
    }

    // Add more test cases for parseOctal method here

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {(byte) 0x80, 0x01, 0x00, 0x00}; // 128 as binary
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 4);
        assertEquals(128, result);
    }

    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = {0x01}; // true
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    // Add more test cases for parseBoolean method here

    // Add test cases for other methods in TarUtils class

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[100];

        long expectedSum = 0;
        for (byte b : header) {
            expectedSum += (b & 0xFF); // considering each byte as unsigned
        }

        long actualSum = TarUtils.computeCheckSum(header);
        assertEquals(expectedSum, actualSum);
    }

}