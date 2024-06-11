import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNormalDecompression() throws Exception {
        byte[] compressedData = "Compressed data".getBytes(); // Replace this with actual compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] result = new byte[compressedData.length];
        int bytesRead = testStream.read(result);
        assertEquals(compressedData.length, bytesRead);
        assertEquals("Decompressed data", new String(result));

        testStream.close();
    }

    @Test(expected = IOException.class)
    public void testDecompressionWithNullInputStream() throws Exception {
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(null, new DeflateParameters());
    }

    @Test(expected = IOException.class)
    public void testDecompressionWithInvalidInputStream() throws Exception {
        byte[] invalidData = new byte[] { 0x00, 0x01, (byte) 0xFF }; // Invalid data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        testStream.close();
    }

    @Test
    public void testDecompressionWithZlibHeader() throws Exception {
        byte[] zlibData = new byte[] { 0x78, 0xDA, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, "Compressed data" }; // Zlib header + compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zlibData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] result = new byte[zlibData.length - 8]; // Subtract the length of the zlib header
        int bytesRead = testStream.read(result);
        assertEquals("Decompressed data", new String(result));

        testStream.close();
    }

    @Test(expected = IOException.class)
    public void testDecompressionWithoutZlibHeader() throws Exception {
        byte[] data = "Compressed data".getBytes(); // Replace this with actual compressed data (without zlib header)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(false));

        testStream.close();
    }

    @Test
    public void testCount() throws Exception {
        byte[] compressedData = "Compressed data".getBytes(); // Replace this with actual compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        int bytesRead = testStream.read();
        long expectedCount = bytesRead == -1 ? 0 : 1;
        long actualCount = testStream.getBytesRead();

        assertEquals(expectedCount, actualCount);

        testStream.close();
    }
}