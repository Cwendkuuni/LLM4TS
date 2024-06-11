import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYW5kIG1hc3Rlcg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes("US-ASCII"));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        // read all data
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // check decoded data
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes("US-ASCII"), outputStream.toByteArray());

        // close streams
        base64InputStream.close();
        outputStream.close();
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        String expectedBase64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYW5kIG1hc3Rlcg==";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputData = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes("US-ASCII");
        Base64 base64 = new Base64(false);

        // encode data
        base64.encode(inputData, 0, inputData.length, outputStream, null);

        // check encoded data
        Assert.assertEquals(expectedBase64EncodedData, outputStream.toString("US-ASCII"));

        // close streams
        outputStream.close();
    }

    @Test
    public void testBase64InputStreamWithCustomLineLengthAndLineSeparator() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYW5kIG1hc3Rlcg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes("US-ASCII"));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, new byte[]{0x0D, 0x0A});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        // read all data
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // check decoded data with custom line length and line separator
        String decodedData = new String(outputStream.toByteArray(), "US-ASCII");
        String expectedDecodedData = "abcdef\r\nghijklm\r\nopqrst\r\nuvwxyz\r\nABCDEF\r\nGHIJKLM\r\nOPQRST\r\nUVWXYZ\r\n01234\r\n56789\r\n+/";
        Assert.assertEquals(expectedDecodedData, decodedData);

        // close streams
        base64InputStream.close();
        outputStream.close();
    }
}