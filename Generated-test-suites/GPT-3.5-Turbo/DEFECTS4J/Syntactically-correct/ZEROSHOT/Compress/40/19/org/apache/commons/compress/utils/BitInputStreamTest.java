import org.junit.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(0);
        Assert.assertEquals(0, result);
        
        result = bitInputStream.readBits(4);
        Assert.assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(0);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
            
            @Override
            public void close() throws IOException {
                throw new IOException("IOException");
            }
        };
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        try {
            bitInputStream.close();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("IOException", e.getMessage());
        }
    }
}