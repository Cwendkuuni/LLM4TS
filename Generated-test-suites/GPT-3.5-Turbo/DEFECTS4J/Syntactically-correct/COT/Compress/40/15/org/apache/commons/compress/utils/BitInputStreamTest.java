
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private static final byte[] TEST_BYTES = {0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0};
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(TEST_BYTES);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        // Read 8 bits, expect 0x12
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x12, result);

        // Read 16 bits, expect 0x1234
        result = bitInputStream.readBits(16);
        Assert.assertEquals(0x1234, result);

        // Read 32 bits, expect 0x12345678
        result = bitInputStream.readBits(32);
        Assert.assertEquals(0x12345678, result);

        // Read 4 bits, expect 0x9
        result = bitInputStream.readBits(4);
        Assert.assertEquals(0x9, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test(expected = IOException.class)
    public void testReadBitsEndOfFile() throws IOException {
        // Skip the entire input stream
        bitInputStream.readBits(64);
        // Try to read more bits, expect an IOException
        bitInputStream.readBits(8);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Read 8 bits, expect 0x12
        bitInputStream.readBits(8);

        // Clear the bit cache
        bitInputStream.clearBitCache();

        // Read 8 bits again, expect 0x12
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x12, result);
    }
}