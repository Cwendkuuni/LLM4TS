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
    public void testDefaultDecoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] actualByteArray = new byte[10];
        int bytesRead = base64InputStream.read(actualByteArray, 0, 10);
        Assert.assertEquals(bytesRead, 10);

        String expectedString = "abcdefghij";
        String actualString = new String(actualByteArray, 0, bytesRead);
        Assert.assertEquals(expectedString, actualString);

        base64InputStream.close();
    }

    /**
     * Test constructor with custom decoding behavior
     */
    @Test
    public void testCustomDecoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream base64OutputStream = new Base64OutputStream(outputStream, true, 64, "\r\n".getBytes());
        base64OutputStream.write("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] actualByteArray = new byte[10];
        int bytesRead = base64InputStream.read(actualByteArray, 0, 10);
        Assert.assertEquals(bytesRead, 10);

        String expectedString = "abcdefghij";
        String actualString = new String(actualByteArray, 0, bytesRead);
        Assert.assertEquals(expectedString, actualString);

        base64InputStream.close();
    }

    /**
     * Test constructor with encoding behavior
     */
    @Test
    public void testEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("abcdefghij".getBytes()), true);

        ByteArrayOutputStream base64OutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(base64OutputStream);
        base64InputStream.transferTo(base64OutputStream);
        base64OutputStream.close();

        String actualString = new String(base64OutputStream.toByteArray(), "UTF-8");
        String expectedString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        Assert.assertEquals(expectedString, actualString);

        base64InputStream.close();
    }

    /**
     * Test read method when EOF is reached
     */
    @Test
    public void testReadEndOfFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] actualByteArray = new byte[10];
        int bytesRead = base64InputStream.read(actualByteArray, 0, 10);
        Assert.assertEquals(bytesRead, 10);

        bytesRead = base64InputStream.read(actualByteArray, 0, 10);
        Assert.assertEquals(bytesRead, -1);

        base64InputStream.close();
    }

    /**
     * Test read method with empty input
     */
    @Test
    public void testReadEmptyInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream base64OutputStream = new Base64OutputStream(outputStream);

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);

        byte[] actualByteArray = new byte[10];
        int bytesRead = base64InputStream.read(actualByteArray, 0, 10);
        Assert.assertEquals(bytesRead, -1);

        base64InputStream.close();
    }

    /**
     * Test markSupported method
     */
    @Test
    public void testMarkSupported() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abcdefghij".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        Assert.assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
    }
}