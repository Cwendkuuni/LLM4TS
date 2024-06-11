import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    // Test for read()
    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {104, 101, 108, 108, 111}; // "hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();
        
        assertEquals(104, result); // ASCII value of 'h'
    }

    // Test for read(byte[], int, int)
    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputBytes = {104, 101, 108, 108, 111}; // "hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[3];
        base64InputStream.read(outputBytes, 0, 3);

        assertArrayEquals(new byte[]{104, 101, 108}, outputBytes); // "hel" in ASCII
    }

    // Test for read() when InputStream is empty
    @Test
    public void testReadEmptyStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals(-1, result);
    }

    // Test for read(byte[], int, int) with invalid arguments
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidArguments() throws IOException {
        byte[] inputBytes = {104, 101, 108, 108, 111}; // "hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[3];
        base64InputStream.read(outputBytes, 0, 5);

        // Should throw IndexOutOfBoundsException
    }

    // Test for read(byte[], int, int) with null byte array
    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        byte[] inputBytes = {104, 101, 108, 108, 111}; // "hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 3);

        // Should throw NullPointerException
    }

    // Test for markSupported() always returning false
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        assertFalse(base64InputStream.markSupported());
    }
}