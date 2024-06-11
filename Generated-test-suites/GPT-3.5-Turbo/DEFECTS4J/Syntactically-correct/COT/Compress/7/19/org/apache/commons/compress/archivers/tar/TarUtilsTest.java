
// JUnit 4 test cases for the TarUtils class
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1: Valid octal string
        byte[] buffer = "123\0".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(123, result);

        // Test case 2: Invalid octal digit
        buffer = "123x\0".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    // Rest of the test methods...

}

// Rest of the test methods...