import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        byte[] testData = {0b00100101, 0b01101101, 0b11110000};
        InputStream testDataStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(testDataStream, ByteOrder.BIG_ENDIAN);

        // Test reading 8 bits
        assertEquals(0b00100101, bitInputStream.readBits(8));

        // Test reading 4 bits
        assertEquals(0b0110, bitInputStream.readBits(4));

        // Test reading 12 bits
        assertEquals(0b111000000011, bitInputStream.readBits(12));

        // Test reading 5 bits
        assertEquals(0b01011, bitInputStream.readBits(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        byte[] testData = {0b00100101, 0b01101101, 0b11110000};
        InputStream testDataStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(testDataStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        byte[] testData = {0b00100101, 0b01101101, 0b11110000};
        InputStream testDataStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(testDataStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        byte[] testData = {0b00100101, 0b01101101, 0b11110000};
        InputStream testDataStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(testDataStream, ByteOrder.LITTLE_ENDIAN);

        // Read some bits to cache
        bitInputStream.readBits(4);

        // Clear the cache
        bitInputStream.clearBitCache();

        // Ensure cache is empty
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = {0b00100101, 0b01101101, 0b11110000};
        InputStream testDataStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(testDataStream, ByteOrder.BIG_ENDIAN);

        // Close the input stream
        bitInputStream.close();
    }

}