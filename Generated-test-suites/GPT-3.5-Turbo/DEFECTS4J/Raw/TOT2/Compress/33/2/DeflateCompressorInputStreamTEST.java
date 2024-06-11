import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = { 0x78, 0x9c, 0x1a, 0x00 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(input);
        assertEquals(0x9c, compressorInputStream.read());
        compressorInputStream.close();
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = { 0x78, 0x9c, 0x1a, 0x00 };
        byte[] buffer = new byte[4];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(input);
        assertEquals(4, compressorInputStream.read(buffer, 0, buffer.length));
        assertArrayEquals(data, buffer);
        compressorInputStream.close();
    }

    @Test
    public void testSkipBytes() throws IOException {
        byte[] data = { 0x78, 0x9c, 0x1a, 0x00 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(input);
        compressorInputStream.skip(2);
        assertEquals(0x1a, compressorInputStream.read());
        compressorInputStream.close();
    }

    @Test
    public void testAvailableBytes() throws IOException {
        byte[] data = { 0x78, 0x9c, 0x1a, 0x00 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(input);
        assertEquals(4, compressorInputStream.available());
        compressorInputStream.read();
        assertEquals(3, compressorInputStream.available());
        compressorInputStream.close();
    }

    @Test
    public void testCloseStream() throws IOException {
        byte[] data = { 0x78, 0x9c, 0x1a, 0x00 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(input);
        compressorInputStream.read();
        compressorInputStream.close();
        assertEquals(-1, compressorInputStream.read());
    }

    @Test
    public void testCheckSignatureWithZlibHeader() {
        byte[] signature = { 0x78, 0x9c };
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        assertTrue(compressorInputStream.checkSignature(signature, 2));
    }

    @Test
    public void testCheckSignatureWithoutZlibHeader() {
        byte[] signature = { 0x1f, 0x8b };
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(compressorInputStream.checkSignature(signature, 2));
    }
}