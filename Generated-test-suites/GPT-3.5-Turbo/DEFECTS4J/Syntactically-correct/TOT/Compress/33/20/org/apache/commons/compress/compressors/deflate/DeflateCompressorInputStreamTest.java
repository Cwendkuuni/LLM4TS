import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    // Test case for read() method
    @Test
    public void testRead() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        Assert.assertEquals(120, input.read());
        input.close();
        Assert.assertEquals(-1, input.read()); // Check that end of stream has reached
    }

    // Test case for read(byte[] buf, int off, int len) method with typical use case
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        byte[] buffer = new byte[5];
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, bytesRead); // Check if correct number of bytes are read
        input.close();
    }

    // Test case for read(byte[] buf, int off, int len) method with edge case
    @Test
    public void testReadWithEdgeBufferSize() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        byte[] buffer = new byte[compressedData.length];
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(compressedData.length, bytesRead); // Check if correct number of bytes are read
        input.close();
    }

    // Test case for skip(long n) method
    @Test
    public void testSkip() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        long bytesSkipped = input.skip(5);
        Assert.assertEquals(5, bytesSkipped);
        input.skip(compressedData.length - 5); // Skip remaining bytes
        Assert.assertEquals(-1, input.read()); // Check that end of stream has reached
        input.close();
    }

    // Test case for available() method
    @Test
    public void testAvailable() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        Assert.assertEquals(compressedData.length, input.available()); // Check initial available bytes
        input.skip(5);
        Assert.assertEquals(compressedData.length - 5, input.available()); // Check available bytes after skipping
        input.close();
    }

    // Test case for close() method
    @Test
    public void testClose() throws IOException {
        byte[] compressedData = {120, -38, 75, 80, 75, 4, 20, 0, 8, 8, 8, -93, 16, 4, 8, -60, -1, 10, -1, 0};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));
        input.close();
        Assert.assertEquals(-1, input.read()); // Check that end of stream has reached after closing
    }

    // Test case for checking the signature
    @Test
    public void testCheckSignature() {
        byte[] signature = {120, -38, 75};
        boolean isZlib = DeflateCompressorInputStream.checkSignature(signature, signature.length);
        Assert.assertTrue(isZlib); // Check that signature is identified as zlib
    }

}