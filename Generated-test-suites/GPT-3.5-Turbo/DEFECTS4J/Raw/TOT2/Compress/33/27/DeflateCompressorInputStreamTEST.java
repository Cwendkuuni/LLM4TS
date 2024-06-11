import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9C, 0x4B, 0x04, 0x00, 0x00, 0x01, 0x00}; // Deflate-compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(75, deflateStream.read()); // First byte after decompression
        deflateStream.close(); // Close the stream
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = {0x78, 0x9C, 0x4B, 0x04, 0x00, 0x00, 0x01, 0x00}; // Deflate-compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte[] buffer = new byte[8];
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(8, deflateStream.read(buffer, 0, 8)); // Read 8 bytes after decompression
        deflateStream.close(); // Close the stream
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9C, 0x4B, 0x04, 0x00, 0x00, 0x01, 0x00}; // Deflate-compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(4, deflateStream.skip(4)); // Skip first 4 bytes after decompression
        deflateStream.close(); // Close the stream
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9C, 0x4B, 0x04, 0x00, 0x00, 0x01, 0x00}; // Deflate-compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(8, deflateStream.available()); // All bytes are available for reading
        deflateStream.close(); // Close the stream
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9C, 0x4B, 0x04, 0x00, 0x00, 0x01, 0x00}; // Deflate-compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        deflateStream.close(); // Close the stream
    }
}