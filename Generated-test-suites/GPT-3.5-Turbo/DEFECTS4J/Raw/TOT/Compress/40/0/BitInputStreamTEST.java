import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.io.IOException;

public class BitInputStreamTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        Assert.assertNotNull(bitInputStream);
    }

    // Test case for the close method
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.close();
        // Since there is no exception, this test will pass if there is no error
    }

    // Test case for the clearBitCache method
    @Test
    public void testClearBitCache() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // Read some bits to partially fill the cache
        try {
            bitInputStream.readBits(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitInputStream.clearBitCache();
        // Since the cache is cleared, this test will pass if the cache size is 0
        Assert.assertEquals(0, bitInputStream.bitsCachedSize);
    }

    // Test case for the readBits method with count = 0
    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // Since the count is 0, no bits should be read and -1 should be returned
        Assert.assertEquals(-1, bitInputStream.readBits(0));
    }

    // Test case for the readBits method with count = 5 (typical use case)
    @Test
    public void testReadBitsWithTypicalCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // The first 5 bits of the input stream are 0001 0010, so the expected output is 0001 (decimal 1)
        Assert.assertEquals(1, bitInputStream.readBits(5));
    }

    // Test case for the readBits method with count = 24 (edge case)
    @Test
    public void testReadBitsWithMaximumCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // The first 24 bits of the input stream are 0001 0010 0011 0100 0101 0110, so the expected output is 0x123456 (decimal 1193046)
        Assert.assertEquals(0x123456, bitInputStream.readBits(24));
    }

    // Test case for the readBits method when end of stream is reached before reading requested bits
    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // The count is 16, but there is only 8 bits in the input stream, so -1 should be returned
        Assert.assertEquals(-1, bitInputStream.readBits(16));
    }

    // Test case for the readBits method with negative count (error scenario)
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // The count is -5, which is negative and should throw IllegalArgumentException
        bitInputStream.readBits(-5);
    }

    // Test case for the readBits method with count greater than maximum cache size (error scenario)
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34, 0x56});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        // The count is 64, which is greater than the maximum cache size and should throw IllegalArgumentException
        bitInputStream.readBits(64);
    }
}