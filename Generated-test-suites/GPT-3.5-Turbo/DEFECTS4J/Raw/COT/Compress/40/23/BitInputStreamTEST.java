
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        // create a dummy InputStream for testing
        byte[] data = {0x01, 0x23, 0x45, 0x67};
        InputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x01, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        // read all the bytes from the input stream
        bitInputStream.readBits(32);
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // read 8 bits and cache them
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x23, result);
    }
}