import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {
  
    @Test
    public void testRead() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        byte[] data = new byte[] { 65, 66, 67 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        int result = baseNCodecInputStream.read();
        
        assertEquals(65, result);
    }
    
    @Test
    public void testReadNullByteArray() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        try {
            baseNCodecInputStream.read(null, 0, 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadNegativeOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        try {
            baseNCodecInputStream.read(new byte[0], -1, 0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadNegativeLength() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        try {
            baseNCodecInputStream.read(new byte[0], 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadInvalidOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        try {
            baseNCodecInputStream.read(new byte[0], 1, 0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadInvalidLength() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        try {
            baseNCodecInputStream.read(new byte[0], 0, 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadLengthZero() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        int result = baseNCodecInputStream.read(new byte[0], 0, 0);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReadResultsZero() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return true; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        int result = baseNCodecInputStream.read();
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testMarkSupported() {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) {}
            
            @Override
            void decode(byte[] b, int off, int len) {}
            
            @Override
            boolean hasData() { return false; }
            
            @Override
            int readResults(byte[] b, int off, int len) { return 0; }
        };
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        boolean result = baseNCodecInputStream.markSupported();
        
        assertFalse(result);
    }
  
}