import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x00, 0x11, 0x62, 0x00, 0xC6, 0xE6, 0x22, 0xA8, 0xAD, 0x03, 0x00, 0x00, 0x00, 0xFF,
                0xFF};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        int result = compressorInputStream.read();
        assertEquals(0x78, result);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] data = new byte[]{};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        int result = compressorInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithMultipleBytes() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x50, 0x4B, 0x05, 0x06};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[4];
        int len = compressorInputStream.read(buffer);
        assertEquals(4, len);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x00, 0x11, 0x62, 0x00, 0xC6, 0xE6, 0x22, 0xA8, 0xAD, 0x03, 0x00, 0x00, 0x00, 0xFF,
            0xFF};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        long result = compressorInputStream.skip(4);
        assertEquals(4, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x00, 0x11, 0x62, 0x00, 0xC6, 0xE6, 0x22, 0xA8, 0xAD, 0x03, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        int result = compressorInputStream.available();
        assertEquals(13, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x00, 0x11, 0x62, 0x00, 0xC6, 0xE6, 0x22, 0xA8, 0xAD, 0x03, 0x00, 0x00, 0x00, 0xFF,
            0xFF};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        compressorInputStream.close();
        assertTrue(compressorInputStream.isClosed());
    }

    @Test
    public void testIsClosed() throws IOException {
        byte[] data = new byte[]{0x78, 0x9C, 0x00, 0x11, 0x62, 0x00, 0xC6, 0xE6, 0x22, 0xA8, 0xAD, 0x03, 0x00, 0x00, 0x00, 0xFF,
            0xFF};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertFalse(compressorInputStream.isClosed());
        compressorInputStream.close();
        assertTrue(compressorInputStream.isClosed());
    }
}