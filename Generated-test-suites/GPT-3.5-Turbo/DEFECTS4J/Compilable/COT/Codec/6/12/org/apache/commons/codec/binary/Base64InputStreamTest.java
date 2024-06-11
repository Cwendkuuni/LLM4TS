package org.apache.commons.codec.binary;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        int result = base64InputStream.read();
        assert result == 72;
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] output = new byte[5];
        int result = base64InputStream.read(output, 0, 5);
        assert result == 5;
        assert new String(output).equals("Hello");
    }

    @Test
    public void testReadWithInvalidByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(null);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        int result = base64InputStream.read();
        assert result == -1;
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] output = new byte[10];
        int result = base64InputStream.read(output, -1, 5);
        assert result == -1;
    }

    @Test
    public void testReadWithInvalidBufferLength() throws IOException {
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] output = new byte[6];
        int result = base64InputStream.read(output, 0, 6);
        assert result == -1;
    }

    @Test
    public void testMarkSupported() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        boolean result = base64InputStream.markSupported();
        assert !result;
    }
}

