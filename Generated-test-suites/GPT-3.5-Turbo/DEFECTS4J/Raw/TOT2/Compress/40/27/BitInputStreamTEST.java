import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClearBitCache() {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        bitInputStream.clearBitCache();

        // Assert
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        long result = bitInputStream.readBits(8);

        // Assert
        assertEquals(0x12, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x12, 0x34});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x12});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Act
        long result = bitInputStream.readBits(16);

        // Assert
        assertEquals(-1, result);
    }
}