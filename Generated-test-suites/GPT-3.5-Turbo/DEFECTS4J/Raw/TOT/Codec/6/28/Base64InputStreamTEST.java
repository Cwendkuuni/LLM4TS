import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    // Test case for read() method with valid byte inputs
    @Test
    public void testRead_ValidBytes_ReturnsByte() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int result = base64InputStream.read();

        Assert.assertEquals(65, result); // Assert that the result is the ASCII value for 'A'
    }

    // Test case for read() method with invalid byte inputs
    @Test
    public void testRead_InvalidBytes_ReturnsNegativeOne() throws IOException {
        byte[] input = {300}; // Invalid byte value
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int result = base64InputStream.read();

        Assert.assertEquals(-1, result); // Assert that the result is -1
    }

    // Test case for read(byte[], int, int) method with valid byte inputs
    @Test
    public void testRead_ValidBytesArray_ReturnsBytesCount() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        byte[] output = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int result = base64InputStream.read(output, 0, 2);

        Assert.assertEquals(2, result); // Assert that the result is 2 (number of bytes read)
        Assert.assertArrayEquals(new byte[]{65, 66}, output); // Assert that the output byte array is correct
    }

    // Test case for read(byte[], int, int) method with invalid byte inputs
    @Test(expected = NullPointerException.class)
    public void testRead_InvalidBytesArray_ThrowsNullPointerException() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        byte[] output = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        base64InputStream.read(output, 0, 2);
    }

    // Test case for read(byte[], int, int) method with invalid offsets
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffsets_ThrowsIndexOutOfBoundsException() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        byte[] output = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        base64InputStream.read(output, -1, 2);
    }

    // Test case for read(byte[], int, int) method with invalid lengths
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLengths_ThrowsIndexOutOfBoundsException() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        byte[] output = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        base64InputStream.read(output, 0, -2);
    }

    // Test case for markSupported() method
    @Test
    public void testMarkSupported_ReturnsFalse() throws IOException {
        byte[] input = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        boolean result = base64InputStream.markSupported();

        Assert.assertFalse(result); // Assert that the result is false
    }

}