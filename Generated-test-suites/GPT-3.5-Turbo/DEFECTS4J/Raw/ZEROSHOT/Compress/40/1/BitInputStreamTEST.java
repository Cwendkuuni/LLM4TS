import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] bytes = {0x01, 0x02, 0x03};
        inputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        long result = bitInputStream.readBits(4);
        assertEquals(0x01, result);
    }

    @Test
    public void testReadBitsReturnsNegativeOnEndOfStream() throws IOException {
        long result = bitInputStream.readBits(32);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBitsThrowsIllegalArgumentExceptionForInvalidCount() {
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x01, result);
    }

}