import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA =
            "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

    private static final String TEST_DATA_ENCODED = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

    @Test
    public void testBase64InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[TEST_DATA.length()];
        int length = base64InputStream.read(result);
        assertEquals(TEST_DATA.length(), length);
        assertArrayEquals(TEST_DATA.getBytes(StandardCharsets.UTF_8), result);
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA_ENCODED.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] result = new byte[TEST_DATA.length()];
        int length = base64InputStream.read(result);
        assertEquals(TEST_DATA.length(), length);
        assertArrayEquals(TEST_DATA.getBytes(StandardCharsets.UTF_8), result);
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, '\r', '\n');
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[]{'\\r', '\\n'});

        int length = -1;
        while ((length = base64InputStream.read(singleByte, 0, 1)) != -1) {
            base64OutputStream.write(singleByte[0]);
        }

        byte[] expectedOutput = Base64.encodeBase64(TEST_DATA);
        assertArrayEquals(expectedOutput, outputStream.toByteArray());

        base64InputStream.close();
        base64OutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        base64InputStream.read(); // should read at least one byte
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamInvalidInputWithEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        base64InputStream.read(); // should read at least one byte
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)), false);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    private byte[] singleByte = new byte[1];
}