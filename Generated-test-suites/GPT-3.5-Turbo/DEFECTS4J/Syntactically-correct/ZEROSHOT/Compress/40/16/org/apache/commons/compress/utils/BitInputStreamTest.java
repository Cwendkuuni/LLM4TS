import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        byte[] bytes = {0b10101010, 0b01010101};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        // Assert that the cache is cleared
        // ...
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        long result = bitInputStream.readBits(8);
        // Assert that the result is correct
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-8);
        // Expect an IllegalArgumentException to be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() throws IOException {
        bitInputStream.readBits(64);
        // Expect an IllegalArgumentException to be thrown
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        // Simulate reaching end of stream
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        bitInputStream = new BitInputStream(emptyStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(8);
        // Assert that the result is -1
        // ...
    }
}