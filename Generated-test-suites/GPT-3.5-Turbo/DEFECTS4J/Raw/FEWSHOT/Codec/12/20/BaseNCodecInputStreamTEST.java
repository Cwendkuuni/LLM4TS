import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);

        assertEquals(97, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);

        byte[] buffer = new byte[10];
        int readLen = baseNCodecInputStream.read(buffer, 0, 3);

        assertEquals(1, readLen);
        assertEquals(97, buffer[0]);
    }
    
    @Test
    public void testReadWithNullBuffer() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        assertThrows(NullPointerException.class, () -> {
            baseNCodecInputStream.read(null, 0, 3);
        });
    }
    
    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);

        byte[] buffer = new byte[10];
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
           baseNCodecInputStream.read(buffer, -1, 3); 
        });
    }
    
    @Test
    public void testReadWithInvalidLength() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);

        byte[] buffer = new byte[10];
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
           baseNCodecInputStream.read(buffer, 0, -1); 
        });
    }
    
    @Test
    public void testReadWithInvalidBufferLength() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);

        byte[] buffer = new byte[10];
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
           baseNCodecInputStream.read(buffer, 0, 11); 
        });
    }
    
    @Test
    public void testReadResultsWithEmptyBuffer() throws IOException {
        byte[] encodedData = {};
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        byte[] buffer = new byte[10];
        int readLen = baseNCodecInputStream.read(buffer, 0, 3);
        
        assertEquals(0, readLen);
        assertArrayEquals(new byte[] { 0, 0, 0 }, buffer);
    }
    
    @Test
    public void testReadResultsWithNonEmptyBuffer() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        byte[] buffer = new byte[10];
        int readLen = baseNCodecInputStream.read(buffer, 0, 3);
        
        assertEquals(1, readLen);
        assertArrayEquals(new byte[] { 97, 0, 0 }, buffer);
    }
    
    @Test
    public void testMarkSupported() {
        byte[] encodedData = {};
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] encodedData = { 97, 98, 99 }; // "abc"
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec codec = new BaseNCodec() {
            // Implement the abstract methods for the test
            protected void encode(byte[] raw, int offset, int length, Context context) {
                
            }
            protected void decode(byte[] encoded, int offset, int length, Context context) {
                
            }
            protected boolean hasData() {
                return false;
            }
            protected int readResults(byte[] buffer, int offset, int length) {
                if (length > 0) {
                    buffer[offset] = 97; // 'a'
                    return 1;
                }
                return 0;
            }
        };
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        int available = baseNCodecInputStream.available();
        
        assertEquals(1, available);
    }
}