package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDefaultDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[1024];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(readData, 0, readData.length)) > 0) {
            outputStream.write(readData, 0, bytesRead);
        }

        byte[] expectedData = "Hello World!".getBytes();
        assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] dataToEncode = "Hello World!".getBytes();
        byte[] encodedData;

        byteArrayOutputStream.write("TW96aGVpZ2h0IHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBTYXggW1s=".getBytes());
        byteArrayOutputStream.flush();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[1024];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(readData, 0, readData.length)) > 0) {
            outputStream.write(readData, 0, bytesRead);
        }

        assertArrayEquals(dataToEncode, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamParameters() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] dataToEncode = "Hello World!".getBytes();

        byteArrayOutputStream.write("TW96aGVpZ2h0IHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBTYXggW1s=".getBytes());
        byteArrayOutputStream.flush();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[] { (byte) 0x0D, (byte) 0x0A });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[1024];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(readData, 0, readData.length)) > 0) {
            outputStream.write(readData, 0, bytesRead);
        }

        assertArrayEquals(dataToEncode, outputStream.toByteArray());
        assertEquals(76, base64InputStream.lineLength);
        assertEquals(new byte[] { (byte) 0x0D, (byte) 0x0A }, base64InputStream.lineSeparator);
    }

    @Test
    public void testBase64InputStreamNullInputStream() {
        InputStream inputStream = null;
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNull(base64InputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamNegativeOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(-1, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(new byte[1024], 1024, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamBufferTooSmall() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[2];
        base64InputStream.read(buffer, 0, buffer.length + 1);
    }
}