import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    // Test case for Base64InputStream(InputStream in)
    @Test
    public void testBase64InputStream1() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais);
        try {
            int read = base64InputStream.read();
            assertEquals(72, read);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test case for Base64InputStream(InputStream in, boolean doEncode)
    @Test
    public void testBase64InputStream2() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais, false);
        try {
            int read = base64InputStream.read();
            assertEquals(72, read);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test case for Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
    @Test
    public void testBase64InputStream3() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais, false, 76, null);
        try {
            int read = base64InputStream.read();
            assertEquals(72, read);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test case for read()
    @Test
    public void testRead() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais, false);
        try {
            int read = base64InputStream.read();
            assertEquals(72, read);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test case for read(byte b[], int offset, int len)
    @Test
    public void testReadByteArray() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais, false);
        try {
            byte[] output = new byte[5];
            int read = base64InputStream.read(output, 0, 5);
            assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, output);
            assertEquals(5, read);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test case for markSupported()
    @Test
    public void testMarkSupported() {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais, false);
        boolean markSupported = base64InputStream.markSupported();
        assertFalse(markSupported);
    }
}