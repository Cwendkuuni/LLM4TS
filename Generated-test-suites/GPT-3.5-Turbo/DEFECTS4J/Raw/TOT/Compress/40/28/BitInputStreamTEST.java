import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        // Create a byte array to simulate an InputStream
        byte[] bytes = { 0b01100101, 0b10101010, 0b11110000 };

        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // Create a BitInputStream using the ByteArrayInputStream and ByteOrder
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_typical() throws IOException {
        // Read 8 bits
        long result = bitInputStream.readBits(8);

        // Check if the result is correct
        assertEquals(0b01100101L, result);
    }

    @Test
    public void testReadBits_edge() throws IOException {
        // Read 24 bits
        long result = bitInputStream.readBits(24);

        // Check if the result is correct
        assertEquals(0b011001011010101011110000L, result);
    }

    @Test
    public void testReadBits_error() throws IOException {
        // Attempt to read 32 bits, which is greater than the maximum allowed size
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bitInputStream.readBits(32));

        // Check if the exception message is correct
        assertThat(exception.getMessage(), equalTo("count must not be negative or greater than 63"));
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Read 8 bits to fill the cache
        bitInputStream.readBits(8);

        // Clear the bit cache
        bitInputStream.clearBitCache();

        // Attempt to read 8 bits again
        long result = bitInputStream.readBits(8);

        // Check if the result is correct
        assertEquals(0b10101010L, result);
    }

    @Test
    public void testClose() throws IOException {
        // Close the BitInputStream
        bitInputStream.close();

        // Attempt to read 8 bits after closing
        long result = bitInputStream.readBits(8);

        // Check if the result is -1, indicating end of stream
        assertEquals(-1, result);
    }

    @Test
    public void testByteOrder() throws IOException {
        // Create a new BitInputStream with LITTLE_ENDIAN byte order
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);

        // Read 8 bits
        long result = bitInputStream.readBits(8);

        // Check if the result is correct
        assertEquals(0L, result);
    }
}