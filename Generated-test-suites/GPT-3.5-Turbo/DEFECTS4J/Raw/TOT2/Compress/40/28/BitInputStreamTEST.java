import static org.junit.Assert.*;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        // Given
        InputStream mockInputStream = new MockInputStream();
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        // When
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, byteOrder);

        // Then
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClose() {
        // Given
        InputStream mockInputStream = new MockInputStream();
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, byteOrder);

        // When
        bitInputStream.close();

        // Then
        assertTrue(true); // No exception means success
    }

    @Test
    public void testClearBitCache() {
        // Given
        InputStream mockInputStream = new MockInputStream();
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, byteOrder);

        // When
        bitInputStream.clearBitCache();

        // Then
        assertTrue(true); // No exception means success
    }

    @Test
    public void testReadBits() {
        // Given
        InputStream mockInputStream = new MockInputStream();
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, byteOrder);

        // When
        long result = bitInputStream.readBits(8);

        // Then
        assertEquals(0xFF, result); // Since we are using a MockInputStream it should return 0xFF
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() {
        // Given
        InputStream mockInputStream = new MockInputStream();
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, byteOrder);

        // When
        bitInputStream.readBits(64); // Trying to read more than 63 bits should throw an IllegalArgumentException
    }
}