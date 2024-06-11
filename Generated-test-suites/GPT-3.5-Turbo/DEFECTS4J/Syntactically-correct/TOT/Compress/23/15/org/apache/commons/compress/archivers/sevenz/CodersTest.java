import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        // Test typical use case
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        InputStream result = Coders.addDecoder(in, coder, null);
        byte[] decodedData = getResultData(result);
        Assert.assertArrayEquals(testData, decodedData);

        // Test edge case - unsupported compression method
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        result = Coders.addDecoder(in, coder, null);
        try {
            result.read();
            Assert.fail("IOException should be thrown for unsupported compression method");
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method " +
                    Arrays.toString(SevenZMethod.COPY.getId()), e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() throws IOException {
        // Test typical use case - encryption method supported
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(out, SevenZMethod.LZMA, null);
        result.write(testData);
        result.close();
        byte[] encodedData = out.toByteArray();
        Assert.assertNotEquals(0, encodedData.length);

        // Test typical use case - encryption method not supported
        out = new ByteArrayOutputStream();
        try {
            result = Coders.addEncoder(out, SevenZMethod.AES256SHA256, null);
            Assert.fail("IOException should be thrown for unsupported compression method");
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method " + SevenZMethod.AES256SHA256, e.getMessage());
        }
    }

    @Test
    public void testLZMADecoder() throws IOException {
        // Test typical use case
        byte[] compressedData = {5, 1, 0, 0, 0, 6, -1, -20, 116, 4, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        Coder coder = new Coder();
        coder.properties = new byte[] {9, 0, 0, 0, 0};
        InputStream result = new Coders.LZMADecoder().decode(in, coder, null);
        byte[] decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test edge case - dictionary larger than 4GiB maximum size
        in = new ByteArrayInputStream(compressedData);
        coder.properties = new byte[] {9, -1, -1, -1, -1};
        try {
            result = new Coders.LZMADecoder().decode(in, coder, null);
            Assert.fail("IOException should be thrown for dictionary larger than 4GiB maximum size");
        } catch (IOException e) {
            Assert.assertEquals("Dictionary larger than 4GiB maximum size", e.getMessage());
        }
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        // Test typical use case
        byte[] compressedData = {8, 1, 0, 0, -1, -1};
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        Coder coder = new Coder();
        InputStream result = new Coders.DeflateDecoder().decode(in, coder, null);
        byte[] decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test typical use case - decrypting with password
        in = new ByteArrayInputStream(compressedData);
        byte[] password = "password".getBytes();
        result = new Coders.DeflateDecoder().decode(in, coder, password);
        decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test edge case - unsupported compression method
        in = new ByteArrayInputStream(compressedData);
        coder.properties = new byte[] {9};
        try {
            result = new Coders.DeflateDecoder().decode(in, coder, null);
            Assert.fail("IOException should be thrown for unsupported compression method");
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method", e.getMessage());
        }
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        // Test typical use case
        byte[] compressedData = {-2, 31, 139, 8};
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        Coder coder = new Coder();
        InputStream result = new Coders.BZIP2Decoder().decode(in, coder, null);
        byte[] decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test typical use case - encrypting with password
        in = new ByteArrayInputStream(compressedData);
        byte[] password = "password".getBytes();
        result = new Coders.BZIP2Decoder().decode(in, coder, password);
        decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test edge case - unsupported compression method
        in = new ByteArrayInputStream(compressedData);
        coder.properties = new byte[] {9};
        try {
            result = new Coders.BZIP2Decoder().decode(in, coder, null);
            Assert.fail("IOException should be thrown for unsupported compression method");
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method", e.getMessage());
        }
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException {
        // Test typical use case
        byte[] encryptedData = {1, 2, 3, 4};
        ByteArrayInputStream in = new ByteArrayInputStream(encryptedData);
        Coder coder = new Coder();
        coder.properties = new byte[] {0, 16};
        InputStream result = new Coders.AES256SHA256Decoder().decode(in, coder, null);
        byte[] decodedData = getResultData(result);
        Assert.assertEquals(1024, decodedData.length);

        // Test edge case - reading encrypted files without a password
        in = new ByteArrayInputStream(encryptedData);
        try {
            result = new Coders.AES256SHA256Decoder().decode(in, coder, null);
            Assert.fail("IOException should be thrown when reading encrypted files without a password");
        } catch (IOException e) {
            Assert.assertEquals("Cannot read encrypted files without a password", e.getMessage());
        }

        // Test edge case - decryption error
        in = new ByteArrayInputStream(encryptedData);
        coder.properties = new byte[] {0, 16, 1, 2};
        try {
            result = new Coders.AES256SHA256Decoder().decode(in, coder, "password".getBytes());
            Assert.fail("IOException should be thrown for decryption error");
        } catch (IOException e) {
            Assert.assertEquals("Decryption error (do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)", e.getMessage());
        }
    }

    private byte[] getResultData(InputStream result) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[1024];
        while ((read = result.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    private static class Coder {
        byte[] decompressionMethodId;
        byte[] properties;
    }

}