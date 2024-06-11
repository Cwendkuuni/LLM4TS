import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import static org.junit.Assert.*;

public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    private InputStream mockInputStream;

    @Before
    public void setUp() {
        mockInputStream = new MockInputStream();
        bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(255, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(16));
        assertEquals(-1, bitInputStream.readBits(32));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsTooLargeCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, getBitsCachedSize(bitInputStream));
        assertEquals(0, getBitsCached(bitInputStream));
    }

    private int getBitsCachedSize(BitInputStream bitInputStream) {
        return bitInputStream.bitsCachedSize;
    }

    private long getBitsCached(BitInputStream bitInputStream) {
        return bitInputStream.bitsCached;
    }
}

class MockInputStream extends InputStream {
    private int index;

    @Override
    public int read() {
        if (index < 255) {
            return index++;
        }
        return -1;
    }
}