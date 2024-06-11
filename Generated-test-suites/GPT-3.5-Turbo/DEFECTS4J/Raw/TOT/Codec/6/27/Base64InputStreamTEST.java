import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {

    // Test case for the read() method

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100}; // "Hello, World" in ASCII
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(in);

        assertEquals(72, base64InputStream.read()); // First byte should be 'H'
        assertEquals(101, base64InputStream.read()); // Second byte should be 'e'
        assertEquals(108, base64InputStream.read()); // Third byte should be 'l'

        base64InputStream.close();
    }

    // Test case for the read(byte[], int, int) method

    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputBytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100}; // "Hello, World" in ASCII
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[6];
        base64InputStream.read(outputBytes, 0, 6); // Read 6 bytes into the outputBytes array

        byte[] expectedOutputBytes = {72, 101, 108, 108, 111, 44}; // Expected output bytes
        for (int i = 0; i < 6; i++) {
            assertEquals(expectedOutputBytes[i], outputBytes[i]);
        }

        base64InputStream.close();
    }

    // Test case for the markSupported() method

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);

        Base64InputStream base64InputStream = new Base64InputStream(in);

        assertEquals(false, base64InputStream.markSupported());

        base64InputStream.close();
    }

    // Test case for the Base64InputStream constructor with doEncode parameter as true

    @Test
    public void testEncodeConstructor() throws IOException {
        // Test encoding "Hello, World" in Base64
        byte[] inputBytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100}; // "Hello, World" in ASCII
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(in, true);

        byte[] outputBytes = new byte[20];
        base64InputStream.read(outputBytes, 0, 20);

        String expectedOutput = "SGVsbG8sIFdvcmxk"; // Expected Base64 encoded output
        String actualOutput = new String(outputBytes);

        assertEquals(expectedOutput, actualOutput);

        base64InputStream.close();
    }

    // Test case for the Base64InputStream constructor with lineLength and lineSeparator parameters

    @Test
    public void testLineLengthAndSeparator() throws IOException {
        // Test encoding "Hello, World" in Base64 with line length of 5 and line separator as "--"
        byte[] inputBytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100}; // "Hello, World" in ASCII
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(in, true, 5, "--".getBytes());

        byte[] outputBytes = new byte[20];
        base64InputStream.read(outputBytes, 0, 20);

        String expectedOutput = "SGVsb--G8sIFdv--cmxk"; // Expected Base64 encoded output with line separator
        String actualOutput = new String(outputBytes);

        assertEquals(expectedOutput, actualOutput);

        base64InputStream.close();
    }

}