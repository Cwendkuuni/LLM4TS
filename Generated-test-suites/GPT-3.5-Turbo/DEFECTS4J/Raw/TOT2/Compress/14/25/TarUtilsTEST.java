import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'0', '1', '2', '3', '4', ' '}; // Valid input
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(1234, result);
        
        // Add more test cases for edge cases and error scenarios
        // ...
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] octalBuffer = {'1', '7', '7', '7', ' '}; // Valid octal number
        long octalResult = TarUtils.parseOctalOrBinary(octalBuffer, 0, octalBuffer.length);
        assertEquals(1023, octalResult);
        
        byte[] binaryBuffer = {(byte) 0x9F, 0x00}; // Valid binary number
        long binaryResult = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(159, binaryResult);
        
        // Add more test cases for edge cases and error scenarios
        // ...
    }

    // Add test methods for each public method in TarUtils class

}