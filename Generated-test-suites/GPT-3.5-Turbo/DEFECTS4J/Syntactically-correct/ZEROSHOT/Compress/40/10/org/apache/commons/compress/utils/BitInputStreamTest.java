import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BitInputStreamTest {

    @Mock
    private InputStream mockedInputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        mockedInputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(mockedInputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBits() throws IOException {
        byte[] byteArray = new byte[]{0, 0, 0, 0, 0, 0, 0, 1};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(1);

        assertEquals(1, result);
    }

    @Test
    public void testReadBits_ThrowsExceptionWhenCountNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitInputStream.readBits(-1);
        });
    }

    @Test
    public void testReadBits_ThrowsExceptionWhenCountGreaterThanMaximumCacheSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitInputStream.readBits(64);
        });
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();

        verify(mockedInputStream, times(1)).close();
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}