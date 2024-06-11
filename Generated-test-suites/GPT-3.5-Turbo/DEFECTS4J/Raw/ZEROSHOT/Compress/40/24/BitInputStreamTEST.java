import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Mock
    private InputStream mockInputStream;

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBitsWithPositiveCount() throws IOException {
        byte[] byteArray = new byte[]{0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(4);

        assertEquals(0x1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();

        // Verify that the close method of the underlying InputStream is called
        verify(mockInputStream).close();
    }

}