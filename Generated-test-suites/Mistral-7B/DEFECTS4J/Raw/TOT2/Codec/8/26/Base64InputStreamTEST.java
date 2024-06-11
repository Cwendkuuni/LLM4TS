import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        // Given
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzbyBkYXRlIGlzIG5vdCBmaWxlIG9mIEFJIGZvdWwgdG8gYW5kIHN1YnR5cGUgYW5kIHBsYW5ldC4=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] byteArray = new byte[1024];
        int length = base64InputStream.read(byteArray, 0, byteArray.length);

        // Then
        Assert.assertEquals(length, 1024);
        Base64 base64 = new Base64();
        byte[] expectedDecodedData = base64.decode(base64EncodedData.getBytes(StandardCharsets.UTF_8));
        Assert.assertArrayEquals(expectedDecodedData, byteArray, 0, expectedDecodedData.length);

        // Cleanup
        base64InputStream.close();
        inputStream.close();
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        // Given
        String textToEncode = "Some text to be encoded.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();

        // When
        base64.encode(textToEncode.getBytes(StandardCharsets.UTF_8), outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()));

        // Then
        byte[] byteArray = new byte[1024];
        int length = base64InputStream.read(byteArray, 0, byteArray.length);
        String decodedText = new String(base64.decode(byteArray), StandardCharsets.UTF_8);
        Assert.assertEquals(textToEncode, decodedText);

        // Cleanup
        base64InputStream.close();
        outputStream.close();
    }

    @Test
    public void testBase64InputStreamWithInvalidBase64Data() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Invalid base64 data".getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Then
        byte[] byteArray = new byte[1024];
        int length = base64InputStream.read(byteArray, 0, byteArray.length);
        Assert.assertEquals(-1, length);

        // Cleanup
        base64InputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamWithNullInputStream() {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(null);

        // Then
        // Expected IOException
    }

    @Test
    public void testBase64InputStreamWithEmptyInputStream() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int length = base64InputStream.read(new byte[1024], 0, 1024);

        // Then
        Assert.assertEquals(-1, length);

        // Cleanup
        base64InputStream.close();
        inputStream.close();
    }
}