
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {

    private InputStream testInputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] testData = {0b10011001, 0b10101010, 0b11001100};
        testInputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(testInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        testInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        assertEquals(0b10011001_10101010_11001100L, bitInputStream.readBits(24));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsExceedMaxCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        bitInputStream.readBits(30);
        assertEquals(-1L, bitInputStream.readBits(40));
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        assertEquals(0L, bitInputStream.readBits(24));
    }
}