import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDecoder() throws IOException {
        // Test decoding a base64 encoded input stream
        String base64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIG1vbWVudHNbID0gRnJybWQtdG8gYW5kIHBsYW5ldCwNCiAgICAtLUludGVyc29uYWwlCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Encoded.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        Assert.assertEquals("Decoded data should match original data", "Test Data".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamEncoder() throws IOException {
        // Test encoding a base64 input stream
        String originalData = "Test Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(originalData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        Assert.assertEquals("Encoded data should match expected base64 data", "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIG1vbWVudHNbID0gRnJybWQtdG8gYW5kIHBsYW5ldCwNCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", outputStream.toByteArray());
    }
}