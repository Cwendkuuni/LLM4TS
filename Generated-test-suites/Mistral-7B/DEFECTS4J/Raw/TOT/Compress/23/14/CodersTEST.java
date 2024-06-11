import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testData);
        Coder coder = new Coder(SevenZMethod.COPY, null);
        InputStream decoderStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[testData.length];
        int bytesRead = decoderStream.read(decodedData);
        Assert.assertArrayEquals(testData, decodedData);
        decoderStream.close();
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] testData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testData);
        Coder coder = new Coder(SevenZMethod.LZMA, null);
        InputStream decoderStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[testData.length];
        int bytesRead = decoderStream.read(decodedData);
        Assert.assertArrayEquals(testData, decodedData);
        decoderStream.close();

        // Edge case: large input data
        byte[] largeTestData = new byte[1024 * 1024]; // 1MB
        is = new ByteArrayInputStream(largeTestData);
        decoderStream = Coders.addDecoder(is, coder, null);
        bytesRead = decoderStream.read(decodedData, 0, decodedData.length);
        Assert.assertEquals(1000000, bytesRead);
        decoderStream.close();

        // Error case: unsupported compression method
        InputStream unsupportedIs = new ByteArrayInputStream(testData);
        Coder unsupportedCoder = new Coder(SevenZMethod.BZIP2, null);
        InputStream unsupportedDecoderStream = Coders.addDecoder(unsupportedIs, unsupportedCoder, null);
        int unsupportedBytesRead = unsupportedDecoderStream.read();
        Assert.assertEquals(-1, unsupportedBytesRead);
        unsupportedDecoderStream.close();
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testData);
        Coder coder = new Coder(SevenZMethod.DEFLATE, null);
        InputStream decoderStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[testData.length];
        int bytesRead = decoderStream.read(decodedData);
        Assert.assertArrayEquals(testData, decodedData);
        decoderStream.close();
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] testData = "BZIP2 test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testData);
        Coder coder = new Coder(SevenZMethod.BZIP2, null);
        InputStream decoderStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[testData.length];
        int bytesRead = decoderStream.read(decodedData);
        Assert.assertArrayEquals(testData, decodedData);
        decoderStream.close();
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        // Provide test data and key as environment variables or hardcode them
        byte[] testData = "AES256SHA256 test data".getBytes(StandardCharsets.UTF_8);
        byte[] testKey = "testkey123".getBytes(StandardCharsets.UTF_8);

        InputStream is = new ByteArrayInputStream(testData);
        Coder coder = new Coder(SevenZMethod.AES256SHA256, testKey);
        InputStream decoderStream = Coders.addDecoder(is, coder, testKey);
        byte[] decodedData = new byte[testData.length];
        int bytesRead = decoderStream.read(decodedData);
        Assert.assertArrayEquals(testData, decodedData);
        decoderStream.close();
    }

    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder coder = new Coder(SevenZMethod.COPY, null);
        InputStream decoderStream = Coders.addDecoder(is, coder, null);
        int bytesRead = decoderStream.read();
        Assert.assertEquals(-1, bytesRead);
        decoderStream.close();
    }
}