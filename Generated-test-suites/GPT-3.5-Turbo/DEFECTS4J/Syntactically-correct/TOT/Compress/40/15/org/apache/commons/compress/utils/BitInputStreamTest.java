import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[] {0x01, 0x23, 0x45, 0x67});
    }

    @Test
    public void testConstructor() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertNotNull(bitInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullInputStream() {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testClose() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertTrue(bitInputStream.isClosed());
    }

    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(12);
        assertEquals(0x012, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithGreaterCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(64);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBitsWithLittleEndianByteOrder() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[] {0x01, 0x23, 0x45, 0x67});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(12);
        assertEquals(0x203, result);
    }

    @Test
    public void testReadBitsWithBigEndianByteOrder() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[] {0x01, 0x23, 0x45, 0x67});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(12);
        assertEquals(0x012, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x01, result);
    }

}