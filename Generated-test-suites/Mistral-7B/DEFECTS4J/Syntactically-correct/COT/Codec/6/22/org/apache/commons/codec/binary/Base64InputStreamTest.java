import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private Base64OutputStream base64OutputStream;

    @Before
    public void setUp() {
        byte[] inputData = "HelloWorld".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64OutputStream = new Base64OutputStream(outputStream, false);
        try {
            base64OutputStream.write(inputData, 0, inputData.length);
            base64OutputStream.flush();
            byte[] encodedData = outputStream.toByteArray();
            base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead() throws IOException {
        int readByte;
        for (int i = 0; i < 10 && (readByte = base64InputStream.read()) != -1; i++) {
            Assert.assertTrue("Expected valid byte, but received: " + readByte, readByte >= 0 && readByte <= 127);
        }
        Assert.assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, bytesRead);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue("Expected valid byte, but received: " + buffer[i], buffer[i] >= 0 && buffer[i] <= 127);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeByteArrayOffset() {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testByteArrayLengthExceeded() {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 11);
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() {
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IOException.class)
    public void testExceptionWhileReadingByteArray() throws IOException, InterruptedException {
        byte[] buffer = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        Base64InputStream testBase64InputStream = new Base64InputStream(inputStream, false);
        Thread thread = new Thread(() -> {
            try {
                testBase64InputStream.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                Assert.assertEquals("Unexpected exception: " + e.getMessage(), "Premature end of data", e.getMessage());
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testMarkSupported() throws IOException {
        Assert.assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testReadWithEncodedData() throws IOException {
        byte[] encodedData = base64OutputStream.toByteArray();
        byte[] expectedData = "HelloWorld".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream testBase64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[10];
        int bytesRead = testBase64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(expectedData.length, bytesRead);
        for (int i = 0; i < expectedData.length; i++) {
            Assert.assertEquals("Expected " + expectedData[i] + " but received " + buffer[i], expectedData[i], buffer[i]);
        }
    }
}