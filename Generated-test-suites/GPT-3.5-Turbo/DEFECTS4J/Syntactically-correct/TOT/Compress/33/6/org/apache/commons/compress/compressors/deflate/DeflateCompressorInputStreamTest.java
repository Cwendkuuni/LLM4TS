import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x9C, 0x08, 0x00, 0x10, 0x0D});
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead_ReturnsCorrectValue() throws IOException {
        int result = deflateCompressorInputStream.read();
        assertEquals(0x78, result);
    }

    @Test
    public void testRead_WhenEndOfFile_ReturnsMinusOne() throws IOException {
        int result = deflateCompressorInputStream.read();
        while (result != -1) {
            result = deflateCompressorInputStream.read();
        }
        assertEquals(-1, result);
    }

    @Test
    public void testRead_ByteArray_ReturnsCorrectValues() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = deflateCompressorInputStream.read(buffer);
        assertEquals(6, bytesRead);
        assertArrayEquals(new byte[]{0x78, 0x9C, 0x08, 0x00, 0x10, 0x0D}, buffer);
    }

    @Test
    public void testSkip_SkipsBytes() throws IOException {
        long skippedBytes = deflateCompressorInputStream.skip(4);
        assertEquals(4, skippedBytes);
        int result = deflateCompressorInputStream.read();
        assertEquals(0x10, result);
    }

    @Test
    public void testAvailable_ReturnsAvailableBytes() throws IOException {
        int availableBytes = deflateCompressorInputStream.available();
        assertEquals(6, availableBytes);
    }

    @Test
    public void testClose_ClosesInputStream() throws IOException {
        deflateCompressorInputStream.close();
        int result = deflateCompressorInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testIsZlibHeader_ValidHeader_ReturnsTrue() throws IOException {
        byte[] signature = new byte[]{(byte) 0x78, (byte) 0x9C, (byte) 0x08, (byte) 0x00, (byte) 0x10, (byte) 0x0D};
        boolean isZlibHeader = deflateCompressorInputStream.isZlibHeader(signature, 6);
        assertEquals(true, isZlibHeader);
    }

    @Test
    public void testIsZlibHeader_InvalidHeader_ReturnsFalse() throws IOException {
        byte[] signature = new byte[]{(byte) 0x78, (byte) 0x9C, (byte) 0x08, (byte) 0x00};
        boolean isZlibHeader = deflateCompressorInputStream.isZlibHeader(signature, 4);
        assertEquals(false, isZlibHeader);
    }
}