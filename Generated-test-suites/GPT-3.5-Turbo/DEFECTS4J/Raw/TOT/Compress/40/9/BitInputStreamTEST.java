import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.mockito.Mockito.when;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    private InputStream inputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReadBitsPositive() throws IOException {
        byte[] data = {0b10101010, 0b11001100, 0b11110000}; // 3 bytes to have 24 bits
        inputStream = new ByteArrayInputStream(data);
        
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(8);
        Assert.assertEquals(0b10101010, bits);

        bits = bitInputStream.readBits(8);
        Assert.assertEquals(0b11001100, bits);
        
        bits = bitInputStream.readBits(8);
        Assert.assertEquals(0b11110000, bits);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] data = {0b10101010, 0b11001100}; // 2 bytes only, less than requested in the test

        inputStream = new ByteArrayInputStream(data);
        
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(16); // 16 bits but only 2 bytes available in the stream
        Assert.assertEquals(0b11001100_10101010L, bits);

        bits = bitInputStream.readBits(16); // 16 bits but less than 2 bytes available in the stream
        Assert.assertEquals(-1, bits);
    }

    @Test
    public void testReadBitsCountExceedsMaxCacheSize() throws IOException {
        byte[] data = {0b10101010, 0b11001100};

        inputStream = new ByteArrayInputStream(data);

        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        try {
            bitInputStream.readBits(BitInputStream.MAXIMUM_CACHE_SIZE + 1); // Count exceeds maximum cache size
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) { }

        try {
            bitInputStream.readBits(-1); // Count is negative
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testClearBitCache() throws IOException {
        byte[] data = {0b10101010, 0b11001100}; // 2 bytes

        inputStream = new ByteArrayInputStream(data);

        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(8);
        Assert.assertEquals(0b10101010, bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(8);
        Assert.assertEquals(0b10101010, bits); // Should read the bits again from the stream
    }

    @After
    public void teardown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
    }
}