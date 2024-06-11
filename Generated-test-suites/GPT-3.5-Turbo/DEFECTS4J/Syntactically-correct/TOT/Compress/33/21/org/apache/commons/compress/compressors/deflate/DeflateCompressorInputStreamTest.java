import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] compressedData = {120, -38, 75, -51, 76, -54, 74, -54, -116, 85, 88, 41, 46, 73, 45, 42,
                85, 80, 47, -121, -126, 5, 0, -128, -1};

        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] decompressedData = new byte[50];
        int bytesRead = deflateCompressorInputStream.read(decompressedData);

        assertEquals(25, bytesRead);
        assertArrayEquals("This is a test".getBytes(), decompressedData);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] compressedData = {};

        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] decompressedData = new byte[50];
        int bytesRead = deflateCompressorInputStream.read(decompressedData);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] compressedData = {120, -38, 75, -51, 76, -54, 74, -54, -116, 85, 88, 41, 46, 73, 45, 42,
                85, 80, 47, -121, -126, 5, 0, -128, -1};

        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = deflateCompressorInputStream.skip(10);

        assertEquals(10, bytesSkipped);

        byte[] decompressedData = new byte[20];
        int bytesRead = deflateCompressorInputStream.read(decompressedData);

        assertEquals(15, bytesRead);
        assertArrayEquals("is a test".getBytes(), decompressedData);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] compressedData = {120, -38, 75, -51, 76, -54, 74, -54, -116, 85, 88, 41, 46, 73, 45, 42,
                85, 80, 47, -121, -126, 5, 0, -128, -1};

        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateCompressorInputStream.available();

        assertEquals(25, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        byte[] compressedData = {120, -38, 75, -51, 76, -54, 74, -54, -116, 85, 88, 41, 46, 73, 45, 42,
                85, 80, 47, -121, -126, 5, 0, -128, -1};

        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();

        try {
            deflateCompressorInputStream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException to be thrown
        }
    }

    @Test
    public void testZlibHeaderCheck() throws IOException {
        byte[] compressedData = {120, -38, 75, -51, 76, -54, 74, -54, -116, 85, 88, 41, 46, 73, 45, 42,
                85, 80, 47, -121, -126, 5, 0, -128, -1};

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData));

        boolean validHeader = deflateCompressorInputStream.checkHeader();

        assertTrue(validHeader);
    }
}