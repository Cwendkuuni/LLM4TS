import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits() throws IOException {
        byte[] bytes = { (byte) 0b10101010, (byte) 0b11001100 };
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bytes), ByteOrder.BIG_ENDIAN);
        
        assertEquals(0b1010, bitInputStream.readBits(4));
        assertEquals(0b10101100, bitInputStream.readBits(8));
        assertEquals(0b110011, bitInputStream.readBits(6));
        assertEquals(-1, bitInputStream.readBits(8)); // EOF
    }

    // Test for IllegalArgumentException when count is negative
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }

    // Test for IllegalArgumentException when count is greater than MAXIMUM_CACHE_SIZE (63)
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsLargeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

}