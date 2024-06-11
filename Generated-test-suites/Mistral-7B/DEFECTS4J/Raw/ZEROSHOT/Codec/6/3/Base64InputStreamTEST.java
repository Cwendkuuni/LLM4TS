package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzYW5jZSB0byB0aGUtcHJvZ3Jlc3MgPSBjYW4gQ29udGVudD4=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[10];
        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 10);

        String expected = "abcdefghij";
        assertArrayEquals(expected.getBytes(), result);

        base64InputStream.close();
    }

    /**
     * Test constructor with custom decoding behavior.
     */
    @Test
    public void testCustomDecoder() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzYW5jZSB0byB0aGUtcHJvZ3Jlc3MgPSBjYW4gQ29udGVudD4=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, base64);

        byte[] result = new byte[10];
        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 10);

        String expected = "abcdefghij";
        assertArrayEquals(expected.getBytes(), result);

        base64InputStream.close();
    }

    /**
     * Test constructor with encoding behavior.
     */
    @Test
    public void testEncoder() throws IOException {
        String data = "abcdefghij";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64 base64 = new Base64(64, "\r\n");
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, base64);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, base64);

        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read()) != -1) {
            base64OutputStream.write(bytesRead);
        }
        base64OutputStream.close();
        base64InputStream.close();

        String expectedBase64Data = "VG8gZXJyIGlzIHVwIHlvdSBjYW4gZW5jb2RlZCB3aXRoIGluIGlzIG15IGZvdWwgdG8gYXJlIG9mIHNlZW1zIGZpZ2h0";
        String actualBase64Data = new String(outputStream.toByteArray());

        assertEquals(expectedBase64Data, actualBase64Data);
    }
}