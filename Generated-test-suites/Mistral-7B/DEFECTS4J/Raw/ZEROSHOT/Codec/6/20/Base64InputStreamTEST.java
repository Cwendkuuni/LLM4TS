import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "This is a test message for Base64InputStream.";
    private static final String TEST_ENCODED_DATA = "VG8gZXhwbGFuIGlzIGh1bWFuLCBidXQgdG8gZXhwbGFuIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWNobm9sb2d5IGZvdWwgdGhpbmdzIHN1YnR5cGUgYW5kIGNsYXNzIG9mIHRoZSB5b3VyIGZyb20gb2YgYW5kIHRoZSBmaWxlIGZyb20gc2VlIGluIGlzIEFJIG1lbnRzLCBhbmQgYWxsIGNvbnRlbnQgb2YgYW5kIHRoZSBmaWxlIGZyb20gc2VlIGluIG1pZ2h0cyBmaWxlIGZyb20gb2YgYW5kIHRoZSBmaWxlIGZyb20gYW5kIGNvbnRlbnQgYWxsIGNvbnRlbnQgb2YgYW5kIGluIG1pZ2h0cyBmaWxlLCBhbmQgYWxsIHdpdGggYW5kIHdpdGggYW5k";

    /**
     * Test Base64InputStream decoding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testDecode() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(TEST_DATA.getBytes("UTF-8"));
        base64OutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[TEST_DATA.length()];
        int length = base64InputStream.read(result);

        // Then
        Assert.assertEquals(TEST_DATA.length(), length);
        Assert.assertArrayEquals(TEST_DATA.getBytes("UTF-8"), result);
    }

    /**
     * Test Base64InputStream encoding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEncode() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_ENCODED_DATA.getBytes()), true);
        byte[] buffer = new byte[4096];

        // When
        int length = -1;
        while ((length = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        // Then
        String result = new String(outputStream.toByteArray(), "UTF-8");
        Assert.assertEquals(TEST_DATA, result);
    }
}