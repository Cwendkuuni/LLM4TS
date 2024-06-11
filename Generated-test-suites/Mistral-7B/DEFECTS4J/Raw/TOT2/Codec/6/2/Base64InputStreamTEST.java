import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIHRoZSBzZXBhcmF0b3Igb2YgdG8gYW5kIHJlYWR5IGxpa2UgYW5kIHJlYXQgdG8gb2YgdG8gYW5kIGRhdGEgZXhwYW5kIFN1YnR5cGUgZXhwYW5kIFN1YnR5cGUsIGZvdWwgYW5kIGRhdGEgZXhwYW5kIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int readBytes;

        do {
            readBytes = base64InputStream.read(buffer);
            if (readBytes > 0) {
                outputStream.write(buffer, 0, readBytes);
            }
        } while (readBytes > -1);

        byte[] expectedOutput = new byte[]{"Hello, World!".getBytes()};
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test constructor with custom decoding behavior.
     */
    @Test
    public void testCustomDecoder() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIHRoZSBzZXBhcmF0b3Igb2YgdG8gYW5kIHJlYWR5IGxpa2UgYW5kIHJlYXQgdG8gb2YgdG8gYW5kIGRhdGEgZXhwYW5kIFN1YnR5cGUgZXhwYW5kIFN1YnR5cGUsIGZvdWwgYW5kIGRhdGEgZXhwYW5kIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int readBytes;

        do {
            readBytes = base64InputStream.read(buffer);
            if (readBytes > 0) {
                outputStream.write(buffer, 0, readBytes);
            }
        } while (readBytes > -1);

        byte[] expectedOutput = new byte[]{"SGVsbG8gV29ybGQh".getBytes()}; // Base64 encoded "Hello, World!"
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test constructor with encoding behavior.
     */
    @Test
    public void testEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();

        String input = "Hello, World!";
        byte[] inputBytes = input.getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true, 76, "\r\n".getBytes());

        byte[] buffer = new byte[1024];
        int readBytes;

        do {
            readBytes = base64InputStream.read(buffer);
            if (readBytes > 0) {
                outputStream.write(buffer, 0, readBytes);
            }
        } while (readBytes > -1);

        String expectedOutput = base64.encodeAsString(inputBytes);
        Assert.assertEquals(expectedOutput, new String(outputStream.toByteArray()));
    }

    /**
     * Test reading from an empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
        byte[] buffer = new byte[1024];
        int readBytes = base64InputStream.read(buffer);
        Assert.assertEquals(-1, readBytes);
    }

    /**
     * Test reading from an input stream with invalid input data.
     */
    @Test(expected = IOException.class)
    public void testInvalidInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgYW5kIHRoZSBzZXBhcmF0b3Igb2YgdG8gYW5kIHJlYWR5IGxpa2UgYW5kIHJlYXQgdG8gb2YgdG8gYW5kIGRhdGEgZXhwYW5kIFN1YnR5cGUgZXhwYW5kIFN1YnR5cGUsIGZvdWwgYW5kIGRhdGEgZXhwYW5kIFIuIEVocmxpY2g=".getBytes()); // Invalid Base64 encoded data
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[1024];
        int readBytes = base64InputStream.read(buffer);
    }

    /**
     * Test reading from an input stream with incomplete Base64 encoded data.
     */
    @Test(expected = IOException.class)
    public void testIncompleteInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQ=".getBytes()); // Incomplete Base64 encoded data
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[1024];
        int readBytes = base64InputStream.read(buffer);
    }
}