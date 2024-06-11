import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};
        long result = TarUtils.parseOctal(buffer, 1, 10);
        assertEquals(123456789L, result);
        
        // Edge case: buffer with all NULs
        byte[] allNULs = {0, 0, 0, 0, 0};
        long allNULsResult = TarUtils.parseOctal(allNULs, 0, 5);
        assertEquals(0L, allNULsResult);
        
        // Exception handling: invalid byte detected
        byte[] invalidByte = {'1', '2', '3', '@'};
        try {
            TarUtils.parseOctal(invalidByte, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'T', 'e', 's', 't', 0, ' ', ' ', ' '};
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("Test", result);
        
        // Edge case: buffer with trailing NUL
        byte[] bufferWithNUL = {'H', 'e', 'l', 'l', 'o', 0, 'W', 'o', 'r', 'l', 'd'};
        String resultNUL = TarUtils.parseName(bufferWithNUL, 0, 5);
        assertEquals("Hello", resultNUL);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 4);
        String result = new String(buffer, 0, offset);
        assertEquals("Test\0\0\0\0\0", result);
        
        // Edge case: name longer than buffer
        String longName = "VeryLongName";
        byte[] longBuffer = new byte[5];
        int longOffset = TarUtils.formatNameBytes(longName, longBuffer, 0, 5);
        String longResult = new String(longBuffer, 0, longOffset);
        assertEquals("VeryL", longResult);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 5);
        String result = new String(buffer, 0, offset);
        assertEquals("123456789 \0", result);
        
        // Exception handling: value will not fit in the buffer
        byte[] smallBuffer = new byte[5];
        try {
            TarUtils.formatOctalBytes(value, smallBuffer, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = {'T', 'e', 's', 't', 0, ' ', ' ', ' '};
        long sum = TarUtils.computeCheckSum(header);
        assertEquals(456, sum);
    }

}