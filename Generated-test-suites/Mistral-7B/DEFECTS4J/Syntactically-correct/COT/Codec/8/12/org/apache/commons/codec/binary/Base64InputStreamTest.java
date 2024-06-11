import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior
     */
    @Test
    public void testDefaultDecoderConstruction() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIGZvdWwgdG8gY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBzbyBmYWNpdHk=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            while ((bytesRead = base64InputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] expectedOutput = "Hello World!".getBytes();
            Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test constructor with custom decoding behavior
     */
    @Test
    public void testCustomDecoderConstruction() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIGZvdWwgdG8gY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBzbyBmYWNpdHk=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false); // set to encode, not decode
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            while ((bytesRead = base64InputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] expectedOutput = new byte[0];
            Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test constructor with custom encoding behavior and line length
     */
    @Test
    public void testCustomEncoderConstruction() {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64 base64 = new Base64(76);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, null);
        Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream(1024), base64);
        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            while ((bytesRead = base64InputStream.read(buffer)) != -1) {
                base64OutputStream.write(buffer, 0, bytesRead);
            }
            byte[] expectedOutput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIGZvdWwgdG8gY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBzbyBmYWNpdHk=".getBytes();
            Assert.assertArrayEquals(expectedOutput, base64OutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test reading past EOF
     */
    @Test(expected = IOException.class)
    public void testReadPastEOF() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIGZvdWwgdG8gY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBzbyBmYWNpdHk=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1024];

        try {
            base64InputStream.read(buffer);
            base64InputStream.read(buffer); // should throw IOException
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test the markSupported method
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIGZvdWwgdG8gY29tcHV0ZXIuCiAgICAgICBmaWxlIG1lcmdlIGRhdGEgcGxlYXNlIG1lYW4gYW55IG1lYW4gdGhlIGlzIHVwIHlvdSBzbyBmYWNpdHk=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        Assert.assertFalse(base64InputStream.markSupported());
    }
}