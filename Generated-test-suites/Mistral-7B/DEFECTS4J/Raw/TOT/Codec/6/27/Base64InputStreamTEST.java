package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Hex;
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
     * Test Base64InputStream decoding a known base64 string.
     */
    @Test
    public void testDecodeBase64String() throws IOException {
        String base64String = "Rm9udCAoKTs=";
        byte[] expected = Hex.decodeHex(new String(new byte[]{0x72, 0x65, 0x61, 0x6d, 0x70, 0x6c, 0x65}));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64String.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] result = new byte[expected.length];
        int length = base64InputStream.read(result, 0, result.length);
        Assert.assertEquals(length, expected.length);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test Base64InputStream decoding a malformed base64 string.
     */
    @Test(expected = IOException.class)
    public void testDecodeMalformedBase64String() throws IOException {
        String base64String = "Rm9udCAoKTs=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64String.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] result = new byte[10];
        int length = base64InputStream.read(result, 0, result.length);
    }

    /**
     * Test Base64InputStream encoding a known byte sequence.
     */
    @Test
    public void testEncodeByteSequence() throws IOException {
        byte[] byteSequence = {0x41, 0x42, 0x43};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(Hex.encodeHex(byteSequence)), false, 76, null);
        int length;
        while ((length = base64InputStream.read(singleByte, 0, singleByte.length)) != -1) {
            base64OutputStream.write(singleByte, 0, length);
        }
        base64OutputStream.finish();
        String actual = new String(outputStream.toByteArray());
        String expected = "VGpzdGVtcyBpcyBhIGxpdGVyIGZvdWwgdG8gZW1icm91bmQ=";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test Base64InputStream encoding a large byte sequence.
     */
    @Test
    public void testEncodeLargeByteSequence() throws IOException {
        byte[] byteSequence = new byte[1024 * 1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(byteSequence), false, 76, null);
        int length;
        while ((length = base64InputStream.read(singleByte, 0, singleByte.length)) != -1) {
            base64OutputStream.write(singleByte, 0, length);
        }
        base64OutputStream.finish();
    }
}