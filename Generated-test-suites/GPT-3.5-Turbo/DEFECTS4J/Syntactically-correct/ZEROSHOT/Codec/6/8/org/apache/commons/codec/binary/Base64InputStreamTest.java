import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() throws IOException {
        byte[] encodedData = Base64.encodeBase64("Hello World!".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int byteRead = base64InputStream.read();
        Assert.assertEquals('H', byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals('H', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('l', buffer[2]);
        Assert.assertEquals('l', buffer[3]);
        Assert.assertEquals('o', buffer[4]);
    }

    @Test
    public void testReadLargeByteArray() throws IOException {
        byte[] buffer = new byte[100];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(12, bytesRead);
        Assert.assertEquals('H', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('l', buffer[2]);
        Assert.assertEquals('l', buffer[3]);
        Assert.assertEquals('o', buffer[4]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithOutOfBoundsOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 10, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithOutOfBoundsLength() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, 10);
    }

    @Test
    public void testReadPastEndOfFile() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, bytesRead);
        int eof = base64InputStream.read();
        Assert.assertEquals(-1, eof);
    }

    @Test
    public void testMarkSupported() {
        boolean markSupported = base64InputStream.markSupported();
        Assert.assertFalse(markSupported);
    }

    @Test
    public void testInputStreamEncoding() throws IOException {
        byte[] encodedData = Base64.encodeBase64("Hello World!".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = base64InputStream.read()) != -1) {
            outputStream.write(byteRead);
        }

        byte[] actualData = outputStream.toByteArray();
        byte[] expectedData = Base64.encodeBase64("Hello World!".getBytes());
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testInputStreamDecoding() throws IOException {
        byte[] encodedData = Base64.encodeBase64("Hello World!".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        base64InputStream = new Base64InputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = base64InputStream.read()) != -1) {
            outputStream.write(byteRead);
        }

        byte[] actualData = outputStream.toByteArray();
        byte[] expectedData = "Hello World!".getBytes();
        Assert.assertArrayEquals(expectedData, actualData);
    }
}