package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read();
        assertEquals('T', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 0, 4);
        assertEquals(4, result);
        assertEquals('T', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, -1);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}