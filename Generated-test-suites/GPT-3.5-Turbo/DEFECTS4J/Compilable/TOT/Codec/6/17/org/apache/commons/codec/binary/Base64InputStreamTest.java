package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        byte[] expectedOutputBytes = "SGVsbG8sIFdvcmxkIQ==".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedOutputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        byte[] outputBytes = new byte[inputBytes.length];
        int bytesRead = base64InputStream.read(outputBytes, 0, inputBytes.length);
        
        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] inputBytes = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] outputBytes = new byte[10];
        int bytesRead = base64InputStream.read(outputBytes, 0, 10);

        assertEquals(0, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] outputBytes = new byte[10];
        base64InputStream.read(outputBytes, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] outputBytes = new byte[10];
        base64InputStream.read(outputBytes, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] outputBytes = new byte[10];
        base64InputStream.read(outputBytes, 0, 11);
    }

    @Test
    public void testReadMultipleTimes() throws IOException {
        String inputString = "Hello";
        byte[] inputBytes = inputString.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 4, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        base64InputStream.close();

        assertEquals("SGVsbG8=", outputStream.toString());
    }

    @Test
    public void testReadWithLineSeparator() throws IOException {
        String inputString = "Hello, World!";
        byte[] inputBytes = inputString.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 6, "\r\n".getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        base64InputStream.close();

        assertEquals("SGVsbG,\r\nb3I=\r\n", outputStream.toString());
    }
}