import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9c, (byte) 0xf3, (byte) 0xca, 0x48, (byte) 0xcd, (byte) 0xc9, (byte) 0xc9, 0x57,
                (byte) 0xf2, 0x53, 0x08, 0x7b, (byte) 0xce, 0x30, (byte) 0xc8, 0x2c, (byte) 0xca, 0x49, 0x51, 0x04,
                0x01, 0x00};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);

        int value = compressor.read();
        assertEquals(120, value);
    }

    @Test
    public void testReadWithBufferAndOffsetAndLength() throws IOException {
        byte[] data = {0x78, 0x9c, (byte) 0xf3, (byte) 0xca, 0x48, (byte) 0xcd, (byte) 0xc9, (byte) 0xc9, 0x57,
                (byte) 0xf2, 0x53, 0x08, 0x7b, (byte) 0xce, 0x30, (byte) 0xc8, 0x2c, (byte) 0xca, 0x49, 0x51, 0x04,
                0x01, 0x00};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);

        byte[] buffer = new byte[10];
        int bytesRead = compressor.read(buffer, 0, 10);

        assertEquals(10, bytesRead);
        assertEquals(0x78, buffer[0]);
        assertEquals(0x9c, buffer[1]);
        assertEquals((byte) 0xf3, buffer[2]);
        assertEquals((byte) 0xca, buffer[3]);
        assertEquals(0x48, buffer[4]);
        assertEquals((byte) 0xcd, buffer[5]);
        assertEquals((byte) 0xc9, buffer[6]);
        assertEquals((byte) 0xc9, buffer[7]);
        assertEquals(0x57, buffer[8]);
        assertEquals((byte) 0xf2, buffer[9]);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9c, (byte) 0xf3, (byte) 0xca, 0x48, (byte) 0xcd, (byte) 0xc9, (byte) 0xc9, 0x57,
                (byte) 0xf2, 0x53, 0x08, 0x7b, (byte) 0xce, 0x30, (byte) 0xc8, 0x2c, (byte) 0xca, 0x49, 0x51, 0x04,
                0x01, 0x00};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);

        long skippedBytes = compressor.skip(3);

        assertEquals(3, skippedBytes);

        int value = compressor.read();
        assertEquals((byte) 0xf3, value);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9c, (byte) 0xf3, (byte) 0xca, 0x48, (byte) 0xcd, (byte) 0xc9, (byte) 0xc9, 0x57,
                (byte) 0xf2, 0x53, 0x08, 0x7b, (byte) 0xce, 0x30, (byte) 0xc8, 0x2c, (byte) 0xca, 0x49, 0x51, 0x04,
                0x01, 0x00};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);

        int available = compressor.available();

        assertEquals(23, available);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9c, (byte) 0xf3, (byte) 0xca, 0x48, (byte) 0xcd, (byte) 0xc9, (byte) 0xc9, 0x57,
                (byte) 0xf2, 0x53, 0x08, 0x7b, (byte) 0xce, 0x30, (byte) 0xc8, 0x2c, (byte) 0xca, 0x49, 0x51, 0x04,
                0x01, 0x00};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);

        compressor.close();
        assertTrue(compressor.isClosed());
    }
}