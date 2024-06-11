import static org.junit.Assert.*;
import org.junit.*;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        InputStream inputStream = new InputStream() {
            public int read() {
                return -1;
            }
        };
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
    public void testReadBitsWithNegativeCount() {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }
    
    @Test
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() {
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }
    
    @Test
    public void testReadBitsWithEndOfStream() {
        try {
            long result = bitInputStream.readBits(10);
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testReadBitsWithByteOrderLittleEndian() {
        InputStream inputStream = new InputStream() {
            private int count = 0;
            private int[] bytes = { 0xAA, 0xBB, 0xCC };
            
            public int read() {
                return count < bytes.length ? bytes[count++] : -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        try {
            long result = bitInputStream.readBits(16);
            assertEquals(0xBBAA, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        try {
            bitInputStream.clearBitCache();
            long result = bitInputStream.readBits(8);
            assertEquals(0xCC, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        try {
            long result = bitInputStream.readBits(4);
            assertEquals(0xC, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testReadBitsWithByteOrderBigEndian() {
        InputStream inputStream = new InputStream() {
            private int count = 0;
            private int[] bytes = { 0xAA, 0xBB, 0xCC };
            
            public int read() {
                return count < bytes.length ? bytes[count++] : -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        try {
            long result = bitInputStream.readBits(16);
            assertEquals(0xAABB, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        try {
            bitInputStream.clearBitCache();
            long result = bitInputStream.readBits(8);
            assertEquals(0xCC, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        try {
            long result = bitInputStream.readBits(4);
            assertEquals(0xC, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}