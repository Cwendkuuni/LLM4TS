package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Tests for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64 stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeBase64Stream() throws IOException {
        // Given
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggSwkKCiAgICAtLVBhdGggTmFtZT4=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] actual = new byte[inputStream.available()];
        int read = base64InputStream.read(actual, 0, actual.length);
        target.write(actual, 0, read);

        // Then
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5 };
        assertEquals(expected, target.toByteArray());
    }

    /**
     * Test encoding a Base64 stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncodeBase64Stream() throws IOException {
        // Given
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5 };
        ByteArrayOutputStream source = new ByteArrayOutputStream();
        source.write(input, 0, input.length);
        InputStream inputStream = source.toByteArrayOutputStream();
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        Base64 base64 = new Base64();
        Base64OutputStream base64OutputStream = new Base64OutputStream(target, base64);

        // When
        int read = -1;
        while ((read = inputStream.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();

        // Then
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        assertEquals(expected, target.toString());

        // Clean up
        base64OutputStream.close();
        inputStream.close();
        source.close();
    }

    /**
     * Test decoding an empty Base64 stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeEmptyBase64Stream() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(new byte[0][0]);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int read = base64InputStream.read();

        // Then
        assertEquals(-1, read);
    }

    /**
     * Test decoding a Base64 stream with invalid data.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidBase64Stream() throws IOException {
        // Given
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggSwkKCiAgICAtLVBhdGggSwkKCiAgICAtLVBhdGggSwkK";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When & Then
        int read = base64InputStream.read();
        fail("Expected an IOException");
    }
}