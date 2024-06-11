import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = { 0b01010101, 0b10101010 }; // sample data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits() throws IOException {
        assertEquals(0b0101010110101010L, bitInputStream.readBits(16));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long bits = bitInputStream.readBits(17);
        assertEquals(-1, bits);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        long bits = bitInputStream.readBits(8);
        assertEquals(0b10101010, bits);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertTrue(bitInputStream.isClosed());
    }

}