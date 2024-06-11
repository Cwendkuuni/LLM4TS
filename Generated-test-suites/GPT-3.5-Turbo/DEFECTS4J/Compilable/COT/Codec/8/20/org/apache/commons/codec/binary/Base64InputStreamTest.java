package org.apache.commons.codec.binary;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assert result == 84;
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] data = "This is a test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 1, 3);
        assert result == 3;
        assert buffer[0] == 0;
        assert buffer[1] == 84;
        assert buffer[2] == 104;
        assert buffer[3] == 105;
        assert buffer[4] == 115;
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] data = "Test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] data = "Test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 6, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws IOException {
        byte[] data = "Test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 1, 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] data = "This is a test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 1, 10);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean result = base64InputStream.markSupported();
        assert !result;
    }
}