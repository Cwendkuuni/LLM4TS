import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = "Test Data".getBytes();

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64 base64 = new Base64();

        byte[] encodedData = base64.encode(TEST_DATA);
        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream(encodedData);

        Base64InputStream decoder = new Base64InputStream(encodedInputStream);
        byte[] result = new byte[TEST_DATA.length];

        int bytesRead = decoder.read(result, 0, TEST_DATA.length);
        Assert.assertEquals(TEST_DATA.length, bytesRead);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64 base64 = new Base64(64, "\n".getBytes());

        Base64InputStream encoder = new Base64InputStream(inputStream, true, 64, "\n".getBytes());
        byte[] result = new byte[3 * TEST_DATA.length / 4 + 4]; // including line breaks

        int bytesRead = encoder.read(result, 0, result.length);
        Assert.assertEquals(bytesRead, result.length);

        byte[] decodedData = base64.decode(result);
        Assert.assertArrayEquals(TEST_DATA, decodedData);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamMarkSupported() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream filterInputStream = new Base64InputStream(inputStream);
        filterInputStream.mark(0);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullInput() {
        ByteArrayInputStream inputStream = null;
        Base64InputStream filterInputStream = new Base64InputStream(inputStream);
    }
}