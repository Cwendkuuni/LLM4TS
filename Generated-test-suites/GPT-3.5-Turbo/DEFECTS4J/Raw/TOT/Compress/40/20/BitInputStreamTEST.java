import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws Exception {
        byte[] byteArray = {0x01, 0x02, 0x03, 0x04};
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
    }

    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new FileInputStream("testFile");
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
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
    public void testReadBitsWithPositiveCount() throws IOException {
        long bits = bitInputStream.readBits(5);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(3);
        assertEquals(2, bits);
    }

    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSize() throws IOException {
        long bits = bitInputStream.readBits(63);
        assertEquals(0x10203, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x04, bits);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        long bits = bitInputStream.readBits(64);
        assertEquals(-1, bits);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

}