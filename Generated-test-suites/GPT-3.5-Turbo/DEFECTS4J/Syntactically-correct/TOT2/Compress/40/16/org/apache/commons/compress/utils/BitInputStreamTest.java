import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() throws IOException {
        // Arrange
        byte[] input = { (byte) 0xAA, (byte) 0x55 }; // 10101010 01010101
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(4); // Cache some bits

        // Act
        bitInputStream.clearBitCache();

        // Assert
        assertEquals(0, bitInputStream.readBits(4)); // Check cache is cleared
    }

    @Test
    public void testReadBits() throws IOException {
        // Arrange
        byte[] input = { (byte) 0xAA, (byte) 0x55 }; // 10101010 01010101
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        long result = bitInputStream.readBits(8); // Read 8 bits

        // Assert
        assertEquals(0xAAL, result); // Check if correct bits are read
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        // Arrange
        byte[] input = { (byte) 0xAA, (byte) 0x55 }; // 10101010 01010101
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        long result = bitInputStream.readBits(64); // Try to read 64 bits

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        // Arrange
        byte[] input = { (byte) 0xAA }; // 10101010
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        long result = bitInputStream.readBits(16); // Try to read more bits than available in the stream

        // Assert
        assertEquals(-1L, result); // Check if end of stream is returned
    }

    // Add more test cases here for other methods if needed

}