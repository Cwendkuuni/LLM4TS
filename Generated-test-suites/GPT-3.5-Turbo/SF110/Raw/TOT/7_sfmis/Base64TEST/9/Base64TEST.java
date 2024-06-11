
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Base64Test {

    @Before
    public void setUp() {
        // Set up any necessary data or dependencies
    }

    @Test
    public void testAltBase64ToByteArray_WithValidString_ReturnsByteArray() {
        String validString = "VGhpcyBpcyBhIHRlc3QgY29tcGxleCBzdHJpbmc=";
        byte[] expectedByteArray = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 116, 101, 115, 116, 32, 99, 111, 109, 112, 108, 101, 120, 32, 115, 116, 114, 105, 110, 103};

        byte[] result = Base64.altBase64ToByteArray(validString);

        assertArrayEquals(expectedByteArray, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_WithInvalidString_ThrowsIllegalArgumentException() {
        String invalidString = "InvalidString";

        Base64.altBase64ToByteArray(invalidString);
    }

    @Test
    public void testBase64ToByteArray_WithValidString_ReturnsByteArray() {
        String validString = "VGhpcyBpcyBhIHRlc3QgY29tcGxleCBzdHJpbmc=";
        byte[] expectedByteArray = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 116, 101, 115, 116, 32, 99, 111, 109, 112, 108, 101, 120, 32, 115, 116, 114, 105, 110, 103};

        byte[] result = Base64.base64ToByteArray(validString);

        assertArrayEquals(expectedByteArray, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_WithInvalidString_ThrowsIllegalArgumentException() {
        String invalidString = "InvalidString";

        Base64.base64ToByteArray(invalidString);
    }

    @Test
    public void testByteArrayToAltBase64_WithValidByteArray_ReturnsBase64String() {
        byte[] validByteArray = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 116, 101, 115, 116, 32, 99, 111, 109, 112, 108, 101, 120, 32, 115, 116, 114, 105, 110, 103};
        String expectedBase64String = "VGhpcyBpcyBhIHRlc3QgY29tcGxleCBzdHJpbmc=";

        String result = Base64.byteArrayToAltBase64(validByteArray);

        assertEquals(expectedBase64String, result);
    }

    @Test
    public void testByteArrayToBase64_WithValidByteArray_ReturnsBase64String() {
        byte[] validByteArray = {84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 116, 101, 115, 116, 32, 99, 111, 109, 112, 108, 101, 120, 32, 115, 116, 114, 105, 110, 103};
        String expectedBase64String = "VGhpcyBpcyBhIHRlc3QgY29tcGxleCBzdHJpbmc=";

        String result = Base64.byteArrayToBase64(validByteArray);

        assertEquals(expectedBase64String, result);
    }

    @Test
    public void testMain_ProgramExecution_PrintsBase64StringAndDecodedString() {
        // Redirect system out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String[] args = {};
        Base64.main(args);

        String expectedOutput = "MDEyMzQ1Njc4OQ==\n0123456789\n";
        String actualOutput = outputStream.toString();

        assertEquals(expectedOutput, actualOutput);
    }

}
