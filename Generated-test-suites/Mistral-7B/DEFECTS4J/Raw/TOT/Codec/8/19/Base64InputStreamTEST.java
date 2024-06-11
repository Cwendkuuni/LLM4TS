package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(TEST_DATA);
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] expectedByteArray = ArrayUtils.toPrimitive(TEST_DATA);
        byte[] resultByteArray = new byte[TEST_DATA.length];
        int length = base64InputStream.read(resultByteArray, 0, TEST_DATA.length);

        // Then
        Assert.assertArrayEquals(expectedByteArray, resultByteArray);
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // When
        int bytesRead;
        while ((bytesRead = base64InputStream.read(singleByte, 0, singleByte.length)) != -1) {
            base64OutputStream.write(singleByte, 0, bytesRead);
        }
        base64OutputStream.close();

        // Then
        String base64EncodedData = IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()), "US-ASCII");
        Assert.assertEquals("VG8gZXhpcyBGb3IgdG8gZW1icmFjZSBmaWxlIGlzIG5vdCBwdXItVGhlIG1lZCBpbiB0aGUgc2VwYXJhdGlvbnM=", base64EncodedData);
    }

    @Test(expected = IllegalStateException.class)
    public void testBase64InputStreamError() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ArrayUtils.toPrimitive(TEST_DATA));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // When
        // Nothing to read
        int bytesRead = base64InputStream.read(singleByte, 0, singleByte.length);

        // Then
        base64InputStream.read(singleByte, 0, singleByte.length); // Throws IOException
    }
}