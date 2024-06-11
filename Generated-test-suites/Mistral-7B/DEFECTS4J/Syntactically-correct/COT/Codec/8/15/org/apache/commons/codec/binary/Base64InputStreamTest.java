package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Base64InputStreamTest.java
 */
public class Base64InputStreamTest {

    @org.junit.Test
    public void testBase64InputStreamConstructorWithDecodeByDefault() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] decodedBytes = new byte[4];
        int read = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        assertEquals(read, decodedBytes.length);
        assertEquals("test".getBytes(), decodedBytes);
    }

    @org.junit.Test
    public void testBase64InputStreamConstructorWithEncode() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] encodedBytes = new byte[10];
        int read = base64InputStream.read(encodedBytes, 0, encodedBytes.length);
        assertEquals(read, encodedBytes.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64.encodeBase64(new byte[]{"test".getBytes()}, outputStream);
        assertArrayEquals(outputStream.toByteArray(), encodedBytes);
    }

    @org.junit.Test
    public void testBase64InputStreamConstructorWithLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        byte[] lineSeparator = new byte[]{0x13, 0x10};
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, lineSeparator);
        byte[] decodedBytes = new byte[51];
        int read = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        assertEquals(read, decodedBytes.length);
        assertEquals("test".getBytes(), decodedBytes, 0, decodedBytes.length - 1);
        assertEquals(lineSeparator[0], decodedBytes[decodedBytes.length - 1]);
        assertEquals(lineSeparator[1], decodedBytes[decodedBytes.length - 2]);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullByteArrayInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        byte[] testBytes = new byte[]{"test".getBytes()};
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(testBytes), false);
        byte[] decodedBytes = new byte[4];
        int offset = -1;
        int len = decodedBytes.length;
        base64InputStream.read(decodedBytes, offset, len);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() {
        byte[] testBytes = new byte[]{"test".getBytes()};
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(testBytes), false);
        byte[] decodedBytes = new byte[4];
        int offset = 0;
        int len = -1;
        base64InputStream.read(decodedBytes, offset, len);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeBufferSize() {
        byte[] testBytes = new byte[]{"test".getBytes()};
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(testBytes), false);
        byte[] decodedBytes = new byte[4];
        int offset = 0;
        int len = decodedBytes.length;
        base64InputStream.read(decodedBytes, offset, -1);
    }

    @org.junit.Test(expected = IOException.class)
    public void testIOExceptionDuringRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] decodedBytes = new byte[4];
        int offset = 0;
        int len = decodedBytes.length;
        inputStream.read(); // throw an IOException
        base64InputStream.read(decodedBytes, offset, len);
    }

    @org.junit.Test
    public void testReadOneByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] decodedByte = new byte[1];
        int read = base64InputStream.read(decodedByte, 0, decodedByte.length);
        assertEquals(read, decodedByte.length);
        assertEquals("test".getBytes()[0], decodedByte);
    }

    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] decodedBytes = new byte[4];
        int read = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        assertEquals(read, decodedBytes.length);
        assertEquals("test".getBytes(), decodedBytes);
    }
}