
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, (byte) 0x9C, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int value = deflateInputStream.read();
        
        assertEquals(120, value);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {0x78, (byte) 0x9C, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int bytesRead = deflateInputStream.read(buffer, 0, buffer.length);

        assertEquals(4, bytesRead);
        assertEquals(120, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, (byte) 0x9C, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateInputStream.skip(2);

        assertEquals(2, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, (byte) 0x9C, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateInputStream.available();

        assertEquals(4, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, (byte) 0x9C, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();
    }
    
}
