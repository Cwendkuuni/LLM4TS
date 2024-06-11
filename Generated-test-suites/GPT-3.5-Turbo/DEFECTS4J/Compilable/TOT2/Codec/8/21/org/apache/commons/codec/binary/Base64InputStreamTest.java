package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private byte[] encodedData = "SGVsbG8gV29ybGQh".getBytes(); // "Hello World!" in Base64
    private byte[] decodedData = "Hello World!".getBytes();

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int c;
        StringBuilder sb = new StringBuilder();
        while ((c = base64InputStream.read()) != -1) {
            sb.append((char) c);
        }

        assertEquals("Hello World!", sb.toString());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[decodedData.length];
        int readLen = base64InputStream.read(buffer, 0, buffer.length);

        assertArrayEquals(decodedData, buffer);
        assertEquals(decodedData.length, readLen);
    }

    @Test
    public void testReadWithInvalidOffset() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[decodedData.length];
        try {
            base64InputStream.read(buffer, -1, buffer.length);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testReadWithInvalidLength() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[decodedData.length];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testReadWithNullBuffer() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        try {
            base64InputStream.read(null, 0, 5);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }
}