import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {
    // Test case for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5'};
        long expectedResult = 0177555;
        long actualResult = TarUtils.parseOctal(buffer, 0, buffer.length);
        // Additional test cases for edge and error scenarios can be added here
        assert(expectedResult == actualResult);
    }

    // ... other test cases for each method can be added here
}