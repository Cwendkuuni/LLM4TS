import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        InputStream input = Mockito.mock(InputStream.class);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        Assert.assertEquals(input, bitInputStream.in);
        Assert.assertEquals(ByteOrder.BIG_ENDIAN, bitInputStream.byteOrder);
    }

    // Test case for the close method
    @Test
    public void testClose() throws IOException {
        InputStream input = Mockito.mock(InputStream.class);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.close();

        Mockito.verify(input).close();
    }

    // Test case for the clearBitCache method
    @Test
    public void testClearBitCache() {
        InputStream input = Mockito.mock(InputStream.class);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.clearBitCache();

        Assert.assertEquals(0, bitInputStream.bitsCached);
        Assert.assertEquals(0, bitInputStream.bitsCachedSize);
    }

    // Test case for the readBits method with valid count
    @Test
    public void testReadBitsValidCount() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(16);

        Assert.assertEquals(0x0A05, result);
    }

    // Test case for the readBits method with count equal to zero
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountZero() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(0);
    }

    // Test case for the readBits method with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(-4);
    }

    // Test case for the readBits method with count exceeding maximum cache size
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountExceedsMaximumCacheSize() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(64);
    }

    // Test case for the readBits method reaching end of stream before reading requested number of bits
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(40);

        Assert.assertEquals(-1, result);
    }

    // Test case for the readBits method with BIG_ENDIAN byte order
    @Test
    public void testReadBitsBigEndian() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(24);

        Assert.assertEquals(0x050A15, result);
    }

    // Test case for the readBits method with LITTLE_ENDIAN byte order
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        byte[] data = {0x05, 0x0A, 0x15, 0x2A, 0x55};
        InputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(24);

        Assert.assertEquals(0x150A05, result);
    }

}