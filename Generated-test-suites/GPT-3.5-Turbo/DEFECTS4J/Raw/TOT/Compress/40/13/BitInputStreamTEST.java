import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.io.*;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("test_file.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() {
        try {
            bitInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_PositiveCount() {
        try {
            long result = bitInputStream.readBits(8);
            
            Assert.assertEquals(16777215, result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testReadBits_NegativeCount() {
        try {
            bitInputStream.readBits(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testReadBits_CountGreaterThanMaximumCacheSize() {
        try {
            bitInputStream.readBits(64);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testReadBits_EndOfStream() {
        try {
            long result = bitInputStream.readBits(64);
            
            Assert.assertEquals(-1, result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testClearBitCache() {
        // Assuming test file has 16 bits, and we have read 8 bits
        try {
            bitInputStream.readBits(8);
            bitInputStream.clearBitCache();
            
            long result = bitInputStream.readBits(16);
            
            Assert.assertEquals(65535, result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}