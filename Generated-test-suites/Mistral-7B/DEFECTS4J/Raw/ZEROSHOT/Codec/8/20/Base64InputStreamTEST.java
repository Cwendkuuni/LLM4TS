import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class Base64InputStreamTest {

    private static final String TEST_DATA = "Some data to test Base64 encoding and decoding.";
    private static final String BASE64_ENCODED = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testDataInputStream = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        Base64 base64 = new Base64();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(testDataInputStream.readAllBytes());
        base64OutputStream.flush();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[expected.length];
        int read = base64InputStream.read(actual);
        assertEquals(expected.length, read);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] expectedEncoded = BASE64_ENCODED.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[expectedEncoded.length];
        int read = base64InputStream.read(actual);
        assertEquals(expectedEncoded.length, read);
        assertArrayEquals(expectedEncoded, actual);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamWithInvalidInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        base64InputStream.read();
    }
}