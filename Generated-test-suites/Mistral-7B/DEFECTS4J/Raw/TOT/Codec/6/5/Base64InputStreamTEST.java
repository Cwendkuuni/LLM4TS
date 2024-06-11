package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.Base64;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test construction with default decoding.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testDefaultDecoding() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = new Base64().getEncoder();
        encoder.encode(base64EncodedData.getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] decodedData = new byte[base64InputStream.available()];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Then
        Assert.assertEquals(base64EncodedData.length(), readBytes);
        Assert.assertArrayEquals(base64EncodedData.getBytes(), decodedData);
    }

    /**
     * Test construction with custom encoding.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testCustomEncoding() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryDecoder decoder = new Base64().getDecoder();
        decoder.decode(base64EncodedData.getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] encodedData = new byte[base64InputStream.available()];
        int readBytes = base64InputStream.read(encodedData, 0, encodedData.length);

        // Then
        Assert.assertEquals(base64EncodedData.length(), readBytes);
        Assert.assertArrayEquals(base64EncodedData.getBytes(), encodedData);
    }

    /**
     * Test construction with custom lineLength and lineSeparator.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testCustomLineLengthAndLineSeparator() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryDecoder decoder = new Base64(72, "\r\n").getDecoder();
        decoder.decode(base64EncodedData.getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 72, "\r\n".getBytes());
        byte[] decodedData = new byte[base64InputStream.available()];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Then
        Assert.assertEquals(base64EncodedData.length(), readBytes);
        Assert.assertArrayEquals(base64EncodedData.getBytes(), decodedData);

        // Clean up
        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test reading single byte with default decoding.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testReadSingleByteWithDefaultDecoding() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJy";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = new Base64().getEncoder();
        encoder.encode(base64EncodedData.getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int readByte = base64InputStream.read();

        // Then
        Assert.assertEquals(base64EncodedData.charAt(0), readByte);
    }

    /**
     * Test reading single byte with custom encoding.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testReadSingleByteWithCustomEncoding() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJy";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryDecoder decoder = new Base64().getDecoder();
        decoder.decode(base64EncodedData.getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int readByte = base64InputStream.read();

        // Then
        Assert.assertEquals(base64EncodedData.charAt(0), readByte);
    }

    /**
     * Test reading bytes with empty input stream.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testReadBytesWithEmptyInputStream() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] decodedData = new byte[base64InputStream.available()];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Then
        Assert.assertEquals(0, readBytes);
    }

    /**
     * Test reading bytes with empty byte array.
     * @throws IOException for any I/O errors
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBytesWithEmptyByteArray() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When & Then
        byte[] decodedData = new byte[0];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Assertion fails with IllegalArgumentException
    }

    /**
     * Test reading bytes with negative offset.
     * @throws IOException for any I/O errors
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeOffset() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When & Then
        byte[] decodedData = new byte[1024];
        int readBytes = base64InputStream.read(decodedData, -1, decodedData.length);

        // Assertion fails with IndexOutOfBoundsException
    }

    /**
     * Test reading bytes with negative length.
     * @throws IOException for any I/O errors
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeLength() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When & Then
        byte[] decodedData = new byte[1024];
        int readBytes = base64InputStream.read(decodedData, 0, -1);

        // Assertion fails with IndexOutOfBoundsException
    }

    /**
     * Test reading bytes with byte array length less than available bytes.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testReadBytesWithByteArrayLengthLessThanAvailableBytes() throws IOException {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = new Base64().getEncoder();
        encoder.encode("abcdefghijklmnopqrstuvwxyz".getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] decodedData = new byte[5];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Then
        Assert.assertEquals(5, readBytes);
        Assert.assertArrayEquals("abcde".getBytes(), decodedData);
    }

    /**
     * Test reading bytes with byte array length equal to available bytes.
     * @throws IOException for any I/O errors
     */
    @Test
    public void testReadBytesWithByteArrayLengthEqualToAvailableBytes() throws IOException {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = new Base64().getEncoder();
        encoder.encode("abcdefghijklmnopqrstuvwxyz".getBytes(), byteArrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] decodedData = new byte[10];
        int readBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        // Then
        Assert.assertEquals(10, readBytes);
        Assert.assertArrayEquals("abcdefghijklmnopqrstuvwxyz".getBytes(), decodedData);
    }

    /**
     * Test markSupported with false.
     */
    @Test
    public void testMarkSupportedWithFalse() {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        // When
        boolean canMark = base64InputStream.markSupported();

        // Then
        Assert.assertFalse(canMark);
    }
}